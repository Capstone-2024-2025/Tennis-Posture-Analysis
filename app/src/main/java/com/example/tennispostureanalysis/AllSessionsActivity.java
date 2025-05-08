package com.example.tennispostureanalysis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AllSessionsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SessionAdapter sessionAdapter;
    private List<Session> allSessions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_sessions);

        // Done Button navigates back to FeedbackPage
        View doneButton = findViewById(R.id.done_button);
        doneButton.setOnClickListener(v -> {
            Intent intent = new Intent(AllSessionsActivity.this, FeedbackPage.class);
            startActivity(intent);
        });

        // Setup RecyclerView
        recyclerView = findViewById(R.id.allSessionsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // vertical scroll

        // Get session list from intent
        Intent intent = getIntent();
        allSessions = (List<Session>) intent.getSerializableExtra("sessionList");

        if (allSessions == null) {
            allSessions = new ArrayList<>(); // fallback to empty list
        }

        // Use full-width layout and enable click to feedback
        sessionAdapter = new SessionAdapter(allSessions, false, session -> {
            Intent feedbackIntent = new Intent(AllSessionsActivity.this, FeedbackActivity.class);
            feedbackIntent.putExtra(FeedbackActivity.EXTRA_FEEDBACK, generateFeedbackForSession(session));
            startActivity(feedbackIntent);
        });

        recyclerView.setAdapter(sessionAdapter);
    }

    private String generateFeedbackForSession(Session session) {
        return "Feedback for session recorded on:\n" +
                android.text.format.DateFormat.format("MMM dd, yyyy • h:mm a", session.getTimestamp());
    }
}