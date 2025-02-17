package com.example.tennispostureanalysis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UploadPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_upload_page);

        // Get references to your View elements
        View boxHome = findViewById(R.id.box_home);
        View boxCapture = findViewById(R.id.box_capture);
        View boxUpload = findViewById(R.id.box_upload);
        View boxFeedback = findViewById(R.id.box_feedback);
        ImageView userIcon = findViewById(R.id.user_icon);

        // Set up OnClickListeners for each box
        boxHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to HomePage
                Intent intent = new Intent(UploadPage.this, HomePage.class);
                startActivity(intent);
            }
        });

        boxCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to CapturePage
                Intent intent = new Intent(UploadPage.this, CapturePage.class);
                startActivity(intent);
            }
        });

        boxFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to FeedbackPage
                Intent intent = new Intent(UploadPage.this, FeedbackPage.class);
                startActivity(intent);
            }
        });

        // Set click listener for the user icon
        userIcon.setOnClickListener(this::showUserMenu);
    }

    // Function to show the dropdown menu
    private void showUserMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(R.menu.user_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_profile) {
                startActivity(new Intent(UploadPage.this, ProfilePage.class));
                return true;
            } else if (id == R.id.menu_settings) {
                startActivity(new Intent(UploadPage.this, SettingsPage.class));
                return true;
            } else if (id == R.id.menu_logout) {
                finish();
                return true;
            }
            return false;
        });
        popup.show();

        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/
    }
}