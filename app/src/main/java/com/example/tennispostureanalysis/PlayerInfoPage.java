package com.example.tennispostureanalysis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.app.Dialog;
import android.widget.NumberPicker;
import android.widget.Toast;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class PlayerInfoPage extends AppCompatActivity {

    private EditText fullName, height;
    private TextView saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_player_info_page);

        // Initialize fields
        fullName = findViewById(R.id.full_name);
        height = findViewById(R.id.height);
        saveButton = findViewById(R.id.save_button);

        // Set onClickListener for Height
        height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHeightPicker();
            }
        });

        // Save Button action
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePlayerInfo();
                // Redirect to to UserMenu
                Intent intent = new Intent(PlayerInfoPage.this, UserMenu.class);
                startActivity(intent);
            }
        });
    }

    // Show a dialog with NumberPicker for height selection
    private void showHeightPicker() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_height_picker);
        dialog.setTitle("Select Height");

        NumberPicker feetPicker = dialog.findViewById(R.id.feet_picker);
        NumberPicker inchesPicker = dialog.findViewById(R.id.inches_picker);
        TextView confirmButton = dialog.findViewById(R.id.confirm_button);

        // Configure feet picker (3ft to 7ft)
        feetPicker.setMinValue(3);
        feetPicker.setMaxValue(7);
        feetPicker.setWrapSelectorWheel(true);

        // Configure inches picker (0 to 11)
        inchesPicker.setMinValue(0);
        inchesPicker.setMaxValue(11);
        inchesPicker.setWrapSelectorWheel(true);

        // Set confirm button action
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int feet = feetPicker.getValue();
                int inches = inchesPicker.getValue();
                String selectedHeight = feet + "'" + inches + "\"";
                height.setText(selectedHeight);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void savePlayerInfo() {
        String name = fullName.getText().toString();
        String userHeight = height.getText().toString();

        // Save data locally or send to database
        Toast.makeText(this, "Info Saved!", Toast.LENGTH_SHORT).show();
    }
}
