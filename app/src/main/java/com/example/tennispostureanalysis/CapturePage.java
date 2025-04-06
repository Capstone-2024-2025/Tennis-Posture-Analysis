package com.example.tennispostureanalysis;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.SurfaceTexture;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tennispostureanalysis.PoseLandMark;
import com.example.tennispostureanalysis.R;
import com.example.tennispostureanalysis.ScreenCaptureService;
import com.example.tennispostureanalysis.VideoPreviewActivity;
import com.google.mediapipe.components.CameraHelper;
import com.google.mediapipe.components.CameraXPreviewHelper;
import com.google.mediapipe.components.ExternalTextureConverter;
import com.google.mediapipe.components.FrameProcessor;
import com.google.mediapipe.components.PermissionHelper;
import com.google.mediapipe.formats.proto.LandmarkProto.NormalizedLandmark;
import com.google.mediapipe.formats.proto.LandmarkProto.NormalizedLandmarkList;
import com.google.mediapipe.framework.AndroidAssetUtil;
import com.google.mediapipe.framework.PacketGetter;
import com.google.mediapipe.glutil.EglManager;
import com.google.protobuf.InvalidProtocolBufferException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.lang.Math;

public class CapturePage extends AppCompatActivity {
    private static final String TAG = "CapturePage";

    private static final String BINARY_GRAPH_NAME = "pose_tracking_gpu.binarypb";
    private static final String INPUT_VIDEO_STREAM_NAME = "input_video";
    private static final String OUTPUT_VIDEO_STREAM_NAME = "output_video";
    private static final String OUTPUT_LANDMARKS_STREAM_NAME = "pose_landmarks";
    private static final int NUM_HANDS = 2;
    private static final CameraHelper.CameraFacing CAMERA_FACING = CameraHelper.CameraFacing.FRONT;
    // Flips the camera-preview frames vertically before sending them into FrameProcessor to be
    // processed in a MediaPipe graph, and flips the processed frames back when they are displayed.
    // This is needed because OpenGL represents images assuming the image origin is at the bottom-left
    // corner, whereas MediaPipe in general assumes the image origin is at top-left.
    private static final boolean FLIP_FRAMES_VERTICALLY = true;

    // Removed DISTANCE_THRESHOLD_FEET and autoStartTriggered since auto-record is no longer needed.

    // For MediaProjection (screen recording)
    private static final int REQUEST_CODE_SCREEN_CAPTURE = 1000;
    private MediaProjectionManager projectionManager;
    private MediaProjection mediaProjection;
    private VirtualDisplay virtualDisplay;
    private MediaRecorder mediaRecorder;
    private boolean isRecording = false;

    // File path where the video is saved.

    static {
        System.loadLibrary("mediapipe_jni");
        System.loadLibrary("opencv_java3");
    }

    private SurfaceTexture previewFrameTexture;
    private SurfaceView previewDisplayView;
    private TextView distanceOverlay; // You can still use this for debug or remove if no longer needed.
    private EglManager eglManager;
    private FrameProcessor processor;
    private ExternalTextureConverter converter;
    private ApplicationInfo applicationInfo;
    private CameraXPreviewHelper cameraHelper;
    private String recordedFilePath;


    private static final double DISTANCE_THRESHOLD_FEET = 5.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String captureMode = getIntent().getStringExtra("CAPTURE_MODE");
        Log.d(TAG, "Selected Capture Mode: " + captureMode);
        setContentView(getContentViewLayoutResId());

        previewDisplayView = new SurfaceView(this);
        distanceOverlay = findViewById(R.id.distance_overlay);
        setupPreviewDisplayView();

        Button recordButton = findViewById(R.id.record_button);
        projectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);

        // Only manual record now:
        recordButton.setOnClickListener(v -> {
            if (!isRecording) {
                startScreenCapture();
                recordButton.setText("Stop Recording");
            } else {
                stopRecording();
                recordButton.setText("Start Recording");
            }
        });

        try {
            applicationInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
        } catch (NameNotFoundException e) {
            Log.e(TAG, "Cannot find application info: " + e);
        }
        AndroidAssetUtil.initializeNativeAssetManager(this);
        eglManager = new EglManager(null);
        processor = new FrameProcessor(
                this,
                eglManager.getNativeContext(),
                BINARY_GRAPH_NAME,
                INPUT_VIDEO_STREAM_NAME,
                OUTPUT_VIDEO_STREAM_NAME);
        processor.getVideoSurfaceOutput().setFlipY(FLIP_FRAMES_VERTICALLY);

        // Callback for the pose landmarks.
        processor.addPacketCallback(
                OUTPUT_LANDMARKS_STREAM_NAME,
                (packet) -> {
                    Log.v(TAG, "Received Pose landmarks packet.");
                    try {
                        byte[] landmarksRaw = PacketGetter.getProtoBytes(packet);
                        NormalizedLandmarkList poseLandmarks = NormalizedLandmarkList.parseFrom(landmarksRaw);
                        runOnUiThread(() -> {

                            checkUserDistance(poseLandmarks);
                        });
                        Log.v(TAG, "[TS:" + packet.getTimestamp() + "] " + getPoseLandmarksDebugString(poseLandmarks));
                    } catch (InvalidProtocolBufferException exception) {
                        Log.e(TAG, "Failed to get proto.", exception);
                    }
                }
        );

        PermissionHelper.checkAndRequestCameraPermissions(this);
    }

    protected int getContentViewLayoutResId() {
        return R.layout.activity_capture_page;
    }

    @Override
    protected void onResume() {
        super.onResume();
        converter = new ExternalTextureConverter(eglManager.getContext(), 2);
        converter.setFlipY(FLIP_FRAMES_VERTICALLY);
        converter.setConsumer(processor);
        if (PermissionHelper.cameraPermissionsGranted(this)) {
            startCamera();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        converter.close();
        previewDisplayView.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    protected void onCameraStarted(SurfaceTexture surfaceTexture) {
        previewFrameTexture = surfaceTexture;
        previewDisplayView.setVisibility(View.VISIBLE);
    }

    protected Size cameraTargetResolution() {
        return null;
    }

    public void startCamera() {
        cameraHelper = new CameraXPreviewHelper();
        cameraHelper.setOnCameraStartedListener(
                surfaceTexture -> {
                    onCameraStarted(surfaceTexture);
                });
        CameraHelper.CameraFacing cameraFacing = CameraHelper.CameraFacing.FRONT;
        cameraHelper.startCamera(
                this, cameraFacing, /*unusedSurfaceTexture=*/ null, cameraTargetResolution());
    }

    protected Size computeViewSize(int width, int height) {
        return new Size(width, height);
    }

    protected void onPreviewDisplaySurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Size viewSize = computeViewSize(width, height);
        Size displaySize = cameraHelper.computeDisplaySizeFromViewSize(viewSize);
        boolean isCameraRotated = cameraHelper.isCameraRotated();
        converter.setSurfaceTextureAndAttachToGLContext(
                previewFrameTexture,
                isCameraRotated ? displaySize.getHeight() : displaySize.getWidth(),
                isCameraRotated ? displaySize.getWidth() : displaySize.getHeight());
    }

    private void setupPreviewDisplayView() {
        previewDisplayView.setVisibility(View.GONE);
        ViewGroup viewGroup = findViewById(R.id.preview_display_layout);
        viewGroup.addView(previewDisplayView);
        previewDisplayView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                processor.getVideoSurfaceOutput().setSurface(holder.getSurface());
                Log.d(TAG, "Surface Created");
            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                onPreviewDisplaySurfaceChanged(holder, format, width, height);
                Log.d(TAG, "Surface Changed");
            }
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                processor.getVideoSurfaceOutput().setSurface(null);
                Log.d(TAG, "Surface Destroyed");
            }
        });
    }

    private static String getPoseLandmarksDebugString(NormalizedLandmarkList poseLandmarks) {
        String poseLandmarkStr = "Pose landmarks: " + poseLandmarks.getLandmarkCount() + "\n";
        ArrayList<PoseLandMark> poseMarkers = new ArrayList<>();
        for (NormalizedLandmark landmark : poseLandmarks.getLandmarkList()) {
            PoseLandMark marker = new PoseLandMark(landmark.getX(), landmark.getY(), landmark.getVisibility());
            poseMarkers.add(marker);
        }
        double rightAngle = getAngle(poseMarkers.get(16), poseMarkers.get(14), poseMarkers.get(12));
        double leftAngle = getAngle(poseMarkers.get(15), poseMarkers.get(13), poseMarkers.get(11));
        double rightKnee = getAngle(poseMarkers.get(24), poseMarkers.get(26), poseMarkers.get(28));
        double leftKnee = getAngle(poseMarkers.get(23), poseMarkers.get(25), poseMarkers.get(27));
        double rightShoulder = getAngle(poseMarkers.get(14), poseMarkers.get(12), poseMarkers.get(24));
        double leftShoulder = getAngle(poseMarkers.get(13), poseMarkers.get(11), poseMarkers.get(23));
        Log.v(TAG, "======[Degree Of Position]======\n" +
                "rightAngle :" + rightAngle + "\n" +
                "leftAngle :" + leftAngle + "\n" +
                "rightHip :" + rightKnee + "\n" +
                "leftHip :" + leftKnee + "\n" +
                "rightShoulder :" + rightShoulder + "\n" +
                "leftShoulder :" + leftShoulder + "\n");
        return poseLandmarkStr;
    }

    static double getAngle(PoseLandMark firstPoint, PoseLandMark midPoint, PoseLandMark lastPoint) {
        double result = Math.toDegrees(
                Math.atan2(lastPoint.getY() - midPoint.getY(), lastPoint.getX() - midPoint.getX())
                        - Math.atan2(firstPoint.getY() - midPoint.getY(), firstPoint.getX() - midPoint.getX()));
        result = Math.abs(result);
        if (result > 180) {
            result = (360.0 - result);
        }
        return result;
    }

    ////////////// Screen Recording Methods //////////////

    // Initiates screen capture by starting an activity for result.
    private void startScreenCapture() {
        Intent captureIntent = projectionManager.createScreenCaptureIntent();
        startActivityForResult(captureIntent, REQUEST_CODE_SCREEN_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SCREEN_CAPTURE && resultCode == Activity.RESULT_OK) {
            // Start the foreground service for media projection.
            Intent serviceIntent = new Intent(this, ScreenCaptureService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent);
            } else {
                startService(serviceIntent);
            }
            // Delay to allow the service to start fully.
            new Handler(getMainLooper()).postDelayed(() -> {
                mediaProjection = projectionManager.getMediaProjection(resultCode, data);
                if (mediaProjection == null) {
                    Log.e(TAG, "MediaProjection is null");
                    return;
                }
                // Register a callback before starting capture.
                mediaProjection.registerCallback(new MediaProjection.Callback() {
                    @Override
                    public void onStop() {
                        Log.d(TAG, "MediaProjection stopped");
                        runOnUiThread(() -> stopRecording());
                    }
                }, new Handler(getMainLooper()));

                setupMediaRecorder();
                createVirtualDisplay();
                try {
                    mediaRecorder.start();
                    isRecording = true;
                    Log.d(TAG, "Recording started successfully.");
                } catch (IllegalStateException e) {
                    Log.e(TAG, "MediaRecorder start failed", e);
                    stopRecording(); // Clean up if starting fails.
                }
            }, 500); // Delay of 500ms; adjust as needed.
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setupMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setVideoEncodingBitRate(512 * 1000);
        mediaRecorder.setVideoFrameRate(30);

        // Calculate width/height
        int width = previewDisplayView.getWidth();
        int height = previewDisplayView.getHeight();
        if (width <= 0 || height <= 0) {
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            width = metrics.widthPixels;
            height = metrics.heightPixels;
        }
        mediaRecorder.setVideoSize(width, height);

        try {
           //Insert a row in MediaStore
            Uri videoUri = createVideoUri();
            if (videoUri == null) {
                Log.e(TAG, "Failed to create video Uri in MediaStore!");
                return;
            }

            //Open a file descriptor for that Uri
            ParcelFileDescriptor pfd =
                    getContentResolver().openFileDescriptor(videoUri, "rw");
            if (pfd == null) {
                Log.e(TAG, "Failed to open ParcelFileDescriptor!");
                return;
            }

            //Provide that file descriptor to MediaRecorder
            mediaRecorder.setOutputFile(pfd.getFileDescriptor());
            mediaRecorder.prepare();
            Log.d(TAG, "MediaRecorder prepared. Using Uri: " + videoUri);

            // If we want to pass something to the VideoPreviewActivity,
            // store the Uri as a String or keep it as a field:
            recordedFilePath = videoUri.toString();
        } catch (IOException e) {
            Log.e(TAG, "MediaRecorder prepare failed", e);
        }
    }





    // Creates a VirtualDisplay that directs screen content to the MediaRecorder.
    private void createVirtualDisplay() {
        int width = previewDisplayView.getWidth();
        int height = previewDisplayView.getHeight();
        if (width <= 0 || height <= 0) {
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            width = metrics.widthPixels;
            height = metrics.heightPixels;
            Log.d(TAG, "Using default display dimensions for VirtualDisplay: " + width + "x" + height);
        }
        int dpi = getResources().getDisplayMetrics().densityDpi;
        virtualDisplay = mediaProjection.createVirtualDisplay(
                "ScreenCapture",
                width,
                height,
                dpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mediaRecorder.getSurface(),
                null,
                null
        );
        Log.d(TAG, "VirtualDisplay created.");
    }

    // Stops the recording, releases resources, triggers a media scan,
    // and launches a preview activity.
    private void stopRecording() {
        if (isRecording) {
            try {
                mediaRecorder.stop();
                Log.d(TAG, "Recording stopped successfully.");
            } catch (RuntimeException e) {
                Log.e(TAG, "MediaRecorder stop failed", e);
            }
            mediaRecorder.reset();

            if (virtualDisplay != null) {
                virtualDisplay.release();
            }
            if (mediaProjection != null) {
                mediaProjection.stop();
            }
            isRecording = false;

            MediaScannerConnection.scanFile(
                    this,
                    new String[] { recordedFilePath },
                    null,
                    (path, uri) -> Log.d(TAG, "Scanned " + path + ": " + uri)
            );

            // Launch preview
            Intent previewIntent = new Intent(this, VideoPreviewActivity.class);
            previewIntent.putExtra("VIDEO_PATH", recordedFilePath);
            startActivity(previewIntent);
        }
    }


    private Uri createVideoUri() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Video.Media.TITLE, "MyRecordedVideo");
        values.put(MediaStore.Video.Media.DISPLAY_NAME, "recorded_video.mp4");
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        values.put(MediaStore.Video.Media.DATE_ADDED, System.currentTimeMillis() / 1000);

        // This places the file under e.g. Movies/TennisPostureAnalysis
        values.put(MediaStore.Video.Media.RELATIVE_PATH,
                Environment.DIRECTORY_MOVIES + "/TennisPostureAnalysis");

        return getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
    }



    // Checks the user's distance using shoulder landmarks (for overlay info only).
    private void checkUserDistance(NormalizedLandmarkList poseLandmarks) {
        if (poseLandmarks == null || poseLandmarks.getLandmarkCount() < 12) {
            return;
        }
        NormalizedLandmark leftShoulder = poseLandmarks.getLandmark(11);
        NormalizedLandmark rightShoulder = poseLandmarks.getLandmark(12);

        int viewWidth = previewDisplayView.getWidth();
        if (viewWidth <= 0) {
            return;
        }

        double shoulderPixelDistance = Math.abs((rightShoulder.getX() - leftShoulder.getX()) * viewWidth);

        double calibrationConstant = 1000;
        double estimatedDistanceFeet = calibrationConstant / shoulderPixelDistance;

        // Show "X FEET BACK" if user is closer than 5 ft, else "Position OK"
        if (estimatedDistanceFeet < DISTANCE_THRESHOLD_FEET) {
            double feetNeeded = DISTANCE_THRESHOLD_FEET - estimatedDistanceFeet;
            distanceOverlay.setText(String.format("%.1f FEET BACK", feetNeeded));
        } else {
            distanceOverlay.setText("Position OK");
        }
    }
}