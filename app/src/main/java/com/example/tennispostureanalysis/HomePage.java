package com.example.tennispostureanalysis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class HomePage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // Get references to your View elements
        View boxHome = findViewById(R.id.box_home);
        View boxCapture = findViewById(R.id.box_capture);
        //View boxUpload = findViewById(R.id.box_upload);
        View boxFeedback = findViewById(R.id.box_feedback);
        ImageView userImage = findViewById(R.id.user_image);
        View boxUser = findViewById(R.id.box_user);

        boxCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to CapturePage
                Intent intent = new Intent(HomePage.this, CaptureSelection.class);
                startActivity(intent);
            }
        });

        /*
        boxUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to UploadPage
                Intent intent = new Intent(HomePage.this, UploadPage.class);
                startActivity(intent);
            }
        }); */

        boxFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to FeedbackPage
                Intent intent = new Intent(HomePage.this, FeedbackPage.class);
                startActivity(intent);
            }
        });

        boxUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to UserMenu
                Intent intent = new Intent(HomePage.this, UserMenu.class);
                startActivity(intent);
            }
        });
    }
}

