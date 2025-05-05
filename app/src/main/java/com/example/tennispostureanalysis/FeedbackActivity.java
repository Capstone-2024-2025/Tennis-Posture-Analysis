package com.example.tennispostureanalysis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class FeedbackActivity extends AppCompatActivity {
    private ScrollView scrollView;
    private Button finishButton;
    private TextView feedbackText;

    public static final String EXTRA_FEEDBACK = "FEEDBACK_TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        scrollView = findViewById(R.id.scroll_view);
        finishButton = findViewById(R.id.finish_button);
        feedbackText = findViewById(R.id.feedback_text);

        Intent intent = getIntent();
        String feedback = intent.getStringExtra(EXTRA_FEEDBACK);

        if (feedback != null) {
            feedbackText.setText(feedback);
        } else {
            feedbackText.setText("No feedback available.");
        }

        // Initially hide the Finish button
        finishButton.setVisibility(View.GONE);

        // Detect scrolling to bottom
        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            View child = scrollView.getChildAt(0);
            if (child != null) {
                int diff = (child.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));
                if (diff <= 0) {
                    // Reached the bottom
                    finishButton.setVisibility(View.VISIBLE);
                }
            }
        });

        // Finish button click
        finishButton.setOnClickListener(v -> {
            Intent feedbackIntent = new Intent(FeedbackActivity.this, FeedbackPage.class);
            startActivity(feedbackIntent);
        });
    }
}
