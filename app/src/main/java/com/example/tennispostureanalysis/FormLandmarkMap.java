package com.example.tennispostureanalysis;

import java.util.Arrays;
import java.util.List;

public class FormLandmarkMap {

    public static List<String> getImportantLandmarksForForm(String formType) {
        switch (formType) {
            case "Overhead Serve":
            case "Kick Serve":
            case "Flat Serve":
                return Arrays.asList(
                        "Left Shoulder", "Right Shoulder",
                        "Left Elbow", "Right Elbow",
                        "Left Wrist", "Right Wrist",
                        "Left Hip", "Right Hip",
                        "Left Knee", "Right Knee"
                );
            case "Forehand Swing":
                return Arrays.asList(
                        "Right Shoulder", "Right Elbow", "Right Wrist",
                        "Left Hip", "Right Hip"
                );
            case "Backhand Swing":
                return Arrays.asList(
                        "Left Shoulder", "Left Elbow", "Left Wrist",
                        "Left Hip", "Right Hip"
                );
            case "Volley":
                return Arrays.asList(
                        "Left Shoulder", "Right Shoulder",
                        "Left Wrist", "Right Wrist"
                );
            default:
                return Arrays.asList(); // Empty list for unknown form
        }
    }
}
