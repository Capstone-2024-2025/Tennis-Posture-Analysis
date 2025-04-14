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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_sessions);

        View doneButton = findViewById(R.id.done_button);

        // Done Button action
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to to UserMenu
                Intent intent = new Intent(AllSessionsActivity.this, FeedbackPage.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.allSessionsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Receive the list passed from FeedbackPage
        Intent intent = getIntent();
        List<Session> allSessions = (List<Session>) intent.getSerializableExtra("sessionList");

        if (allSessions == null) {
            allSessions = new ArrayList<>(); // fallback
        }

        sessionAdapter = new SessionAdapter(allSessions);
        recyclerView.setAdapter(sessionAdapter);
    }
}
