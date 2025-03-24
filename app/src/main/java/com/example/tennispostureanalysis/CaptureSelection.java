package com.example.tennispostureanalysis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class CaptureSelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_selection);

        View handheldButton = findViewById(R.id.button_handheld);
        View standButton = findViewById(R.id.button_stand);


        View.OnClickListener listener = v -> {
            String captureMode = "";
            if (v.getId() == R.id.button_handheld) {
                captureMode = "Handheld";
            } else if (v.getId() == R.id.button_stand) {
                captureMode = "Stand";
            }

            Intent intent = new Intent(CaptureSelection.this, CapturePage.class);
            intent.putExtra("CAPTURE_MODE", captureMode);
            startActivity(intent);
        };

        handheldButton.setOnClickListener(listener);
        standButton.setOnClickListener(listener);

    }
}
