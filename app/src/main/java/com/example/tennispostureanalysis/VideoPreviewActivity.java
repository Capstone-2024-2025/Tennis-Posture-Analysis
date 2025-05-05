package com.example.tennispostureanalysis;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.mediapipe.formats.proto.LandmarkProto;

public class VideoPreviewActivity extends AppCompatActivity {

    public static final String EXTRA_VIDEO_PATH = "VIDEO_PATH";

    private String videoPath, captureMode, formType, analysisMode;
    private boolean llmResponseReceived = false;

    private VideoView videoView;
    private LinearLayout buttonLayout, swingButtonGroup, progressLayout;
    private ProgressBar progressBar;
    private TextView progressText;

    private LandmarkProto.NormalizedLandmarkList poseLandmarks;

    // Swing mode buttons
    private Button drawButton, redrawButton, saveButton, graphButton, swingReRecordButton;

    private boolean isDrawingNow = false;  // Toggle state

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_preview);

        // Get passed data
        Intent intent = getIntent();
        videoPath = intent.getStringExtra(EXTRA_VIDEO_PATH);
        captureMode = intent.getStringExtra("CAPTURE_MODE");
        formType = intent.getStringExtra("FORM_TYPE");
        analysisMode = intent.getStringExtra("MODE");

        // View bindings
        videoView = findViewById(R.id.video_view);
        buttonLayout = findViewById(R.id.button_layout);
        swingButtonGroup = findViewById(R.id.swing_button_group);
        progressLayout = findViewById(R.id.progress_layout);
        progressBar = findViewById(R.id.progress_bar);
        progressText = findViewById(R.id.progress_text);

        Button analyzeButton = findViewById(R.id.analyze_button);
        Button reRecordButton = findViewById(R.id.re_record_button);

        drawButton = findViewById(R.id.draw_button);
        redrawButton = findViewById(R.id.redraw_button);
        saveButton = findViewById(R.id.save_button);
        graphButton = findViewById(R.id.generate_graph_button);
        swingReRecordButton = findViewById(R.id.swing_re_record_button);

        // Load video
        if (videoPath != null) {
            videoView.setVideoURI(Uri.parse(videoPath));
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            videoView.requestFocus();
        } else {
            Toast.makeText(this, "Video file not found!", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Handle landmarks if available
        byte[] landmarksBytes = intent.getByteArrayExtra("POSE_LANDMARKS");
        if (landmarksBytes != null) {
            try {
                poseLandmarks = LandmarkProto.NormalizedLandmarkList.parseFrom(landmarksBytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Show appropriate buttons based on mode
        if ("SWING".equals(analysisMode)) {
            buttonLayout.setVisibility(View.GONE);
            swingButtonGroup.setVisibility(View.VISIBLE);
        } else {
            buttonLayout.setVisibility(View.VISIBLE);
            swingButtonGroup.setVisibility(View.GONE);
        }

        // Form analysis buttons
        analyzeButton.setOnClickListener(v -> startAnalyzeProcess());
        reRecordButton.setOnClickListener(v -> reRecordVideo());

        // Swing mode buttons
        drawButton.setOnClickListener(v -> {
            isDrawingNow = !isDrawingNow;
            CapturePage.isDrawing = isDrawingNow;
            drawButton.setText(isDrawingNow ? "Stop Drawing" : "Draw");
            Toast.makeText(this, isDrawingNow ? "Drawing started" : "Drawing stopped", Toast.LENGTH_SHORT).show();
        });

        redrawButton.setOnClickListener(v -> {
            Toast.makeText(this, "Redraw triggered", Toast.LENGTH_SHORT).show();
            // Reset logic can go here
        });

        saveButton.setOnClickListener(v -> {
            Toast.makeText(this, "Swing saved", Toast.LENGTH_SHORT).show();
            // Save current wristPathPoints (append to swing list)
        });

        graphButton.setOnClickListener(v -> {
            Toast.makeText(this, "Graph generated", Toast.LENGTH_SHORT).show();
            // Launch graph visualization activity
        });

        swingReRecordButton.setOnClickListener(v -> reRecordVideo());
    }

    private void startAnalyzeProcess() {
        videoView.setVisibility(View.GONE);
        buttonLayout.setVisibility(View.GONE);
        progressLayout.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        progressText.setText("Analyzing: 0%");

        new Thread(() -> {
            int progress = 0;
            try {
                while (progress < 100) {
                    if (llmResponseReceived) progress = 100;
                    else progress += 2;

                    int current = progress;
                    runOnUiThread(() -> {
                        progressBar.setProgress(current);
                        progressText.setText("Analyzing: " + current + "%");
                    });

                    if (progress >= 100) break;
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        analyzeWithLLM();
    }

    private void analyzeWithLLM() {
        new Thread(() -> {
            String summary = PoseFeatureExtractor.extractPoseFeatures(poseLandmarks, formType);
            String prompt = PromptBuilder.buildPrompt(formType, summary);
            String feedback = LLMRequestManager.getCoachingFeedback(prompt);
            llmResponseReceived = true;

            runOnUiThread(() -> {
                Intent intent = new Intent(this, FeedbackActivity.class);
                intent.putExtra(FeedbackActivity.EXTRA_FEEDBACK, feedback);
                startActivity(intent);
            });
        }).start();
    }

    private void reRecordVideo() {
        Intent intent = new Intent(this, CapturePage.class);
        intent.putExtra("CAPTURE_MODE", captureMode);
        intent.putExtra("FORM_TYPE", formType);
        intent.putExtra("MODE", analysisMode);
        startActivity(intent);
        finish();
    }
}