package com.example.tennispostureanalysis;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FeedbackPage extends AppCompatActivity {

    private RecyclerView sessionRecyclerView;
    private SessionAdapter sessionAdapter;
    private List<Session> sessionList;

    private RecyclerView drillsRecycler;
    private DrillAdapter drillAdapter;
    private List<Drill> drillList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_feedback_page);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.BLACK);
            getWindow().setNavigationBarColor(Color.BLACK);// makes top bar black
        }


        // Get references to your View elements
        View boxHome = findViewById(R.id.box_home);
        View boxCapture = findViewById(R.id.box_capture);
        //View boxUpload = findViewById(R.id.box_upload);
        View boxFeedback = findViewById(R.id.box_feedback);
        //ImageView userImage = findViewById(R.id.user_image);
        //View boxUser = findViewById(R.id.box_user);

        // Set up OnClickListeners for each box
        boxHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to HomePage
                Intent intent = new Intent(FeedbackPage.this, HomePage.class);
                startActivity(intent);
            }
        });

        boxCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to CapturePage
                Intent intent = new Intent(FeedbackPage.this, CapturePage.class);
                startActivity(intent);
            }
        });

        // "View All" Button
        findViewById(R.id.viewAll).setOnClickListener(v -> {
            Intent intent = new Intent(FeedbackPage.this, AllSessionsActivity.class);
            intent.putExtra("sessionList", (ArrayList<Session>) sessionList);
            startActivity(intent);
        });

        /*
        boxUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to UploadPage
                Intent intent = new Intent(FeedbackPage.this, UploadPage.class);
                startActivity(intent);
            }
        }); */

        // Sessions RecyclerView
        sessionRecyclerView = findViewById(R.id.sessionRecyclerView);
        sessionRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        sessionList = SessionStorage.sessions;
        sessionAdapter = new SessionAdapter(sessionList, true);
        sessionRecyclerView.setAdapter(sessionAdapter);

        // Drills RecyclerView
        drillsRecycler = findViewById(R.id.drillsRecycler);
        drillsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        drillList = getDrillData();
        drillAdapter = new DrillAdapter(this, drillList);
        drillsRecycler.setAdapter(drillAdapter);
    }

    private List<Drill> getDrillData() {
        List<Drill> list = new ArrayList<>();
        list.add(new Drill("Serve Drill", "https://www.youtube.com/watch?v=Bcqi_M9aPmg"));
        list.add(new Drill("Backhand Drill", "https://www.youtube.com/watch?v=4ldOe300rMk"));
        list.add(new Drill("Footwork Drill", "https://www.youtube.com/watch?v=eGWhONP7558"));
        list.add(new Drill("Perfect Forehand", "https://www.youtube.com/watch?v=yyQ-v4V3NU8"));
        list.add(new Drill("Tennis For Beginners", "https://www.youtube.com/watch?v=1NDXZpbw3qA&list=PLEd-bhJ7w1qn9mi1EHLlM4QIb2kah_Lt-"));
        list.add(new Drill("High Intensity Performance Drills", "https://www.youtube.com/watch?v=yhcQieaHnyQ&list=PLJP-Wou-v6z3GXigpvRUDNbgk2R1mD6tU&index=15"));
        list.add(new Drill("Train with Roger Federer","https://www.youtube.com/watch?v=AX9nYKiUK4A&list=PLJP-Wou-v6z3GXigpvRUDNbgk2R1mD6tU&index=31"));
        list.add(new Drill("Hand-Eye Coordination", "https://www.youtube.com/watch?v=zQAFeheat0w&list=PLJP-Wou-v6z3GXigpvRUDNbgk2R1mD6tU&index=32"));
        list.add(new Drill("Perfect Posture", "https://www.youtube.com/watch?v=o-PiQX1P0QY"));
        list.add(new Drill("Pro Tennis Drills", "https://www.youtube.com/watch?v=DL6elnRWo5o&list=PLJP-Wou-v6z3GXigpvRUDNbgk2R1mD6tU&index=10"));
        return list;
    }

    private List<Session> getDummySessions() {
        List<Session> dummyList = new ArrayList<>();
        dummyList.add(new Session("https://via.placeholder.com/150", System.currentTimeMillis()));
        dummyList.add(new Session("https://via.placeholder.com/150/FF0000", System.currentTimeMillis() - 3600000)); // 1 hour ago
        dummyList.add(new Session("https://via.placeholder.com/150/00FF00", System.currentTimeMillis() - 86400000)); // 1 day ago
        return dummyList;
    }
}