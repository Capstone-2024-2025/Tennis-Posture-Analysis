package com.example.tennispostureanalysis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class UserMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_menu);

        // Get references to your View elements
        //View  = findViewById(R.id.);
        Button logoutButton = findViewById(R.id.logout_button);
        Button playerButton = findViewById(R.id.info_button);
        View doneButton = findViewById(R.id.done_button);

        // Navigate to HomePage.java on button click
        playerButton.setOnClickListener(view -> {
            Intent intent = new Intent(UserMenu.this, PlayerInfoPage.class);
            startActivity(intent);
        });

        // Done Button action
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to to UserMenu
                Intent intent = new Intent(UserMenu.this, HomePage.class);
                startActivity(intent);
            }
        });

        // Done Button action
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to to UserMenu
                Intent intent = new Intent(UserMenu.this, LogInPage.class);
                startActivity(intent);
            }
        });

    }
}