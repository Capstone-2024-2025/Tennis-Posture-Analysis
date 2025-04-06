package com.example.tennispostureanalysis;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

public class VideoPreviewActivity extends AppCompatActivity {

    public static final String EXTRA_VIDEO_PATH = "VIDEO_PATH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_preview);



        VideoView videoView = findViewById(R.id.video_view);
        String videoPath = getIntent().getStringExtra(EXTRA_VIDEO_PATH);
        if (videoPath != null) {
            videoView.setVideoURI(Uri.parse(videoPath));
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            videoView.requestFocus();
        } else {
            finish();
        }



    }

}

