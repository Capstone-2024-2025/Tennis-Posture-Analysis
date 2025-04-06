package com.example.tennispostureanalysis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;

public class HomePage extends AppCompatActivity {
    private LineChart lineChart;
    private String[] tips = {
            "Focus on racket swing speed",
            "Maintain a balanced stance",
            "Keep your eyes on the ball",
            "Follow through on every shot",
            "Improve footwork for better positioning",
            "You are great"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // Get references to your View elements
        //View boxHome = findViewById(R.id.box_home);
        View boxCapture = findViewById(R.id.box_capture);
        View boxFeedback = findViewById(R.id.box_feedback);
        ImageView userImage = findViewById(R.id.user_image);
        View boxUser = findViewById(R.id.box_user);

        //Initialize the chart
        lineChart = findViewById(R.id.lineChart);
        setupLineChart();
        //updateSessionHighlights();

        // Set click listeners
        boxCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to CapturePage
                Intent intent = new Intent(HomePage.this, CaptureSelection.class);
                startActivity(intent);
            }
        });

        /*
        boxUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to UploadPage
                Intent intent = new Intent(HomePage.this, UploadPage.class);
                startActivity(intent);
            }
        }); */

        boxFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to FeedbackPage
                Intent intent = new Intent(HomePage.this, FeedbackPage.class);
                startActivity(intent);
            }
        });

        boxUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to UserMenu
                Intent intent = new Intent(HomePage.this, UserMenu.class);
                startActivity(intent);
            }
        });

        // Coach's Tip of the Day Section
        TextView tipText = findViewById(R.id.tip_text);

        //Set a random tip from the list
        Random random = new Random();
        String randomTip = tips[random.nextInt(tips.length)];
        tipText.setText("⚡ " + randomTip);
    }

    private  void setupLineChart() {

        // Generate sample data for Swing Speed (in mph) over 5 sessions
        List<Entry> dataPoints = new ArrayList<>();
        dataPoints.add(new Entry(0, 65));   // Session 1: 65 mph
        dataPoints.add(new Entry(1, 70));   // Session 2: 70 mph
        dataPoints.add(new Entry(2, 78));   // Session 3: 78 mph (best)
        dataPoints.add(new Entry(3, 72));   // Session 4: 72 mph
        dataPoints.add(new Entry(4, 75));   // Session 5: 75 mph

        // Create a dataset
        LineDataSet dataSet = new LineDataSet(dataPoints, "Swing Speed (mph)");
        dataSet.setColor(getResources().getColor(android.R.color.holo_green_dark));
        dataSet.setValueTextColor(getResources().getColor(android.R.color.black));
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(5f);

        // Set data to the chart
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate(); // Refresh chart
        lineChart.getDescription().setEnabled(false);

        lineChart.setExtraOffsets(10f, 10f, 30f, 10f); // Add padding to the right

        // Customize X-axis formatter for session labels
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new SessionValueFormatter()); // Use custom formatter

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setGranularity(1f);
        lineChart.getAxisRight().setEnabled(false); // Hide right Y-axis
    }

    // ✅ Create a separate ValueFormatter class for session labels
    public static class SessionValueFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            return "Session " + ((int) value + 1);
        }
    }
}

