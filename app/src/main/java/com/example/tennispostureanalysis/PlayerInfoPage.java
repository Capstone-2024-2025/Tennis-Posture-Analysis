package com.example.tennispostureanalysis;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PlayerInfoPage extends AppCompatActivity {

    // UI components
    private EditText fullName, height;
    private TextView saveButton;
    private String handedness = "Right"; // default handedness

    // Firebase instances
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Enables full screen edge-to-edge support
        setContentView(R.layout.activity_player_info_page);

        // Link UI elements to layout
        fullName = findViewById(R.id.full_name);
        height = findViewById(R.id.height);
        saveButton = findViewById(R.id.save_button);

        // Initialize Firebase Authentication and Database reference
        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("players");

        // Show height picker dialog on height field click
        height.setOnClickListener(v -> showHeightPicker());

        // Handedness selection (set by clicking Left or Right buttons)
        findViewById(R.id.left_button).setOnClickListener(v -> handedness = "Left");
        findViewById(R.id.right_button).setOnClickListener(v -> handedness = "Right");

        // Save button stores the user info and goes to the UserMenu
        saveButton.setOnClickListener(v -> {
            savePlayerInfo(); // Save to Firebase
            startActivity(new Intent(PlayerInfoPage.this, UserMenu.class)); // Navigate to menu
        });
    }

    private void savePlayerInfo() {
        String name = fullName.getText().toString().trim();
        String userHeight = height.getText().toString().trim();

        // Check for empty fields
        if (name.isEmpty() || userHeight.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Use the current Firebase Auth user ID (or "test_user" fallback)
        String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : "test_user";

        // Create a Player object with the entered data
        Player player = new Player(name, userHeight, handedness);

        // Save player object to Firebase under /players/{userId}
        userRef.child(userId).setValue(player)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(this, "Info saved to Firebase!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to save: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void showHeightPicker() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_height_picker);
        dialog.setTitle("Select Height");

        // Get the pickers and confirm button from the dialog layout
        NumberPicker feetPicker = dialog.findViewById(R.id.feet_picker);
        NumberPicker inchesPicker = dialog.findViewById(R.id.inches_picker);
        TextView confirmButton = dialog.findViewById(R.id.confirm_button);

        // Set valid range for feet and inches
        feetPicker.setMinValue(3);
        feetPicker.setMaxValue(7);
        feetPicker.setWrapSelectorWheel(true);

        inchesPicker.setMinValue(0);
        inchesPicker.setMaxValue(11);
        inchesPicker.setWrapSelectorWheel(true);

        // When confirm is clicked, format the height and update the field
        confirmButton.setOnClickListener(v -> {
            int feet = feetPicker.getValue();
            int inches = inchesPicker.getValue();
            height.setText(feet + "'" + inches + "\"");
            dialog.dismiss();
        });
        dialog.show();
    }
}