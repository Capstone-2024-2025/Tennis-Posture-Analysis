package com.example.tennispostureanalysis;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.text.InputType;

import androidx.appcompat.app.AppCompatActivity;

public class LogInPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_page);

        Button btnSignIn = findViewById(R.id.btn_sign_in);
        EditText passwordEditText = findViewById(R.id.password);
        ImageButton togglePasswordVisibility = findViewById(R.id.togglePasswordVisibility);

        // Navigate to HomePage.java on button click
        btnSignIn.setOnClickListener(view -> {
            Intent intent = new Intent(LogInPage.this, HomePage.class);
            startActivity(intent);
        });

        // Toggle password visibility on ImageButton click
        togglePasswordVisibility.setOnClickListener(view -> {
            if (passwordEditText.getInputType() == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                togglePasswordVisibility.setImageResource(android.R.drawable.ic_menu_close_clear_cancel); // Change icon to 'Hide'
            } else {
                passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                togglePasswordVisibility.setImageResource(android.R.drawable.ic_menu_view); // Change icon to 'Show'
            }
            passwordEditText.setSelection(passwordEditText.getText().length()); // Move cursor to the end
        });
    }
}
