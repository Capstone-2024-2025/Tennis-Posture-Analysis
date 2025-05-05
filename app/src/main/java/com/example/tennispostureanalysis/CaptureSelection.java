package com.example.tennispostureanalysis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class CaptureSelection extends AppCompatActivity {

    private LinearLayout formSelectionLayout;
    private Spinner formTypeSpinner;
    private Button continueButton;
    private Button getReadyButton;

    private LinearLayout recordingTypeLayout;
    private Button recordFormButton, trackSwingButton, swingContinueButton;
    private View swingTooltip;

    private View handheldButton, standButton;

    private String captureMode = "";
    private String selectedFormType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_selection);

        // Find views
        handheldButton = findViewById(R.id.button_handheld);
        standButton = findViewById(R.id.button_stand);
        formSelectionLayout = findViewById(R.id.form_selection_layout);
        formTypeSpinner = findViewById(R.id.form_type_spinner);
        continueButton = findViewById(R.id.continue_button);
        getReadyButton = findViewById(R.id.get_ready_button);
        recordingTypeLayout = findViewById(R.id.recording_type_layout);
        recordFormButton = findViewById(R.id.record_form_button);
        trackSwingButton = findViewById(R.id.track_swing_button);
        swingTooltip = findViewById(R.id.swing_instruction_bubble);
        swingContinueButton = findViewById(R.id.swing_continue_button);

        // Spinner setup
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.form_types_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        formTypeSpinner.setAdapter(adapter);

        // Handheld selected
        handheldButton.setOnClickListener(v -> {
            captureMode = "Handheld";
            handheldButton.setEnabled(false);
            standButton.setEnabled(true);
            swingTooltip.setVisibility(View.GONE);

            formSelectionLayout.setVisibility(View.GONE);
            continueButton.setVisibility(View.GONE);
            getReadyButton.setVisibility(View.GONE);
            recordingTypeLayout.setVisibility(View.VISIBLE);
        });

        // Stand selected
        standButton.setOnClickListener(v -> {
            captureMode = "Stand";
            standButton.setEnabled(false);
            handheldButton.setEnabled(true);
            swingTooltip.setVisibility(View.GONE);

            formSelectionLayout.setVisibility(View.GONE);
            continueButton.setVisibility(View.GONE);
            getReadyButton.setVisibility(View.GONE);
            recordingTypeLayout.setVisibility(View.VISIBLE);
        });

        // Record Form
        recordFormButton.setOnClickListener(v -> {
            swingTooltip.setVisibility(View.GONE);
            formSelectionLayout.setVisibility(View.VISIBLE);
            if (captureMode.equals("Handheld")) {
                continueButton.setVisibility(View.VISIBLE);
                getReadyButton.setVisibility(View.GONE);
            } else {
                getReadyButton.setVisibility(View.VISIBLE);
                continueButton.setVisibility(View.GONE);
            }
        });

        // Track Swing
        trackSwingButton.setOnClickListener(v -> {
            swingTooltip.setVisibility(View.VISIBLE);
            formSelectionLayout.setVisibility(View.GONE);
            continueButton.setVisibility(View.GONE);
            getReadyButton.setVisibility(View.GONE);
        });

        swingContinueButton.setOnClickListener(v -> {
            Intent intent = new Intent(CaptureSelection.this, CapturePage.class);
            intent.putExtra("CAPTURE_MODE", captureMode);
            intent.putExtra("MODE", "SWING");
            startActivity(intent);
        });

        continueButton.setOnClickListener(v -> {
            selectedFormType = formTypeSpinner.getSelectedItem().toString();
            goToCapturePage("FORM");
        });

        getReadyButton.setOnClickListener(v -> {
            selectedFormType = formTypeSpinner.getSelectedItem().toString();
            goToCapturePage("FORM");
        });
    }

    private void goToCapturePage(String mode) {
        Intent intent = new Intent(CaptureSelection.this, CapturePage.class);
        intent.putExtra("CAPTURE_MODE", captureMode);
        intent.putExtra("FORM_TYPE", selectedFormType);
        intent.putExtra("MODE", mode);
        startActivity(intent);
    }
}
