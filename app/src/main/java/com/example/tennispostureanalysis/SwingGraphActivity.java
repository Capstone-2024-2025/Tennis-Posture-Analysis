package com.example.tennispostureanalysis;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class SwingGraphActivity extends AppCompatActivity {

    // Key used to pass the swing type via intent
    public static final String EXTRA_SWING_TYPE = "SWING_TYPE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swing_graph);

        // Reference to the LineChart view in the layout
        LineChart chart = findViewById(R.id.backhandChart);

        // Get the swing type passed from VideoPreviewActivity
        String swingType = getIntent().getStringExtra(EXTRA_SWING_TYPE);

        // Set the description label at the bottom right of the chart
        chart.getDescription().setText(swingType + " Swing Comparison");

        // Prepare the dataset(s) to display based on swing type
        List<LineDataSet> dataSets = getSwingDataSets(swingType);

        // Set the data for the chart
        LineData data = new LineData(dataSets.toArray(new LineDataSet[0]));
        chart.setData(data);

        // Animate the chart horizontally
        chart.animateX(1000);

        // Configure the legend to show line symbols
        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);

        // Move the X-axis labels to the bottom
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // Disable the right Y-axis (left stays enabled)
        chart.getAxisRight().setEnabled(false);
    }

     /* Generates different dummy swing data depending on swing type.
      This would typically be replaced by actual landmark data. */
    private List<LineDataSet> getSwingDataSets(String swingType) {
        List<LineDataSet> sets = new ArrayList<>();

        // Each swing type gets a unique colored dataset
        if ("Forehand".equals(swingType)) {
            sets.add(makeSet(new float[][]{{0, 1}, {1, 2}, {2, 2.5f}}, "User Forehand", R.color.holo_blue_light));
        } else if ("Serve".equals(swingType)) {
            sets.add(makeSet(new float[][]{{0, 0}, {1, 1.5f}, {2, 3}}, "User Serve", R.color.holo_orange_light));
        } else { // Default: backhand
            sets.add(makeSet(new float[][]{{0, 3.5f}, {1, 2.8f}, {2, 2}}, "User Backhand", R.color.holo_green_dark));
        }

        return sets;
    }


     /* Utility method to convert a 2D array of coordinates into a LineDataSet.
     Array of [x, y] points
     Label for the line
     colorRes Resource ID of the line color
     Configured LineDataSet */
    private LineDataSet makeSet(float[][] coords, String label, int colorRes) {
        List<Entry> entries = new ArrayList<>();
        for (float[] point : coords) {
            entries.add(new Entry(point[0], point[1]));
        }

        LineDataSet set = new LineDataSet(entries, label);
        set.setColor(getColor(colorRes)); // Set line color
        set.setCircleRadius(4f);          // Circle radius at data points
        return set;
    }
}