package com.example.tennispostureanalysis;

import com.google.mediapipe.formats.proto.LandmarkProto.NormalizedLandmark;
import com.google.mediapipe.formats.proto.LandmarkProto.NormalizedLandmarkList;

import java.util.List;

public class PoseFeatureExtractor {

    public static double calculateAngle(NormalizedLandmark first, NormalizedLandmark mid, NormalizedLandmark last) {
        double result = Math.toDegrees(
                Math.atan2(last.getY() - mid.getY(), last.getX() - mid.getX()) -
                        Math.atan2(first.getY() - mid.getY(), first.getX() - mid.getX())
        );
        result = Math.abs(result);
        if (result > 180) {
            result = 360.0 - result;
        }
        return result;
    }

    public static String extractPoseFeatures(NormalizedLandmarkList landmarks, String formType) {
        if (landmarks == null || landmarks.getLandmarkCount() < 30) {
            return "Pose data incomplete.";
        }

        List<String> importantLandmarks = FormLandmarkMap.getImportantLandmarksForForm(formType);

        if (importantLandmarks.isEmpty()) {
            return "No landmark mapping available for this form type.";
        }

        // Retrieve all necessary landmarks
        NormalizedLandmark leftShoulder = landmarks.getLandmark(11);
        NormalizedLandmark rightShoulder = landmarks.getLandmark(12);
        NormalizedLandmark leftElbow = landmarks.getLandmark(13);
        NormalizedLandmark rightElbow = landmarks.getLandmark(14);
        NormalizedLandmark leftWrist = landmarks.getLandmark(15);
        NormalizedLandmark rightWrist = landmarks.getLandmark(16);
        NormalizedLandmark leftHip = landmarks.getLandmark(23);
        NormalizedLandmark rightHip = landmarks.getLandmark(24);
        NormalizedLandmark leftKnee = landmarks.getLandmark(25);
        NormalizedLandmark rightKnee = landmarks.getLandmark(26);
        NormalizedLandmark leftAnkle = landmarks.getLandmark(27);
        NormalizedLandmark rightAnkle = landmarks.getLandmark(28);

        StringBuilder sb = new StringBuilder();
        sb.append("Observations:\n");

        // Only compute and append the important angles/distances
        if (importantLandmarks.contains("Left Shoulder") && importantLandmarks.contains("Right Shoulder")) {
            double shoulderTilt = Math.toDegrees(Math.atan2(
                    rightShoulder.getY() - leftShoulder.getY(),
                    rightShoulder.getX() - leftShoulder.getX()));
            sb.append("- Shoulder tilt angle: ").append(String.format("%.1f", shoulderTilt)).append(" degrees\n");
        }

        if (importantLandmarks.contains("Left Shoulder") && importantLandmarks.contains("Left Elbow") && importantLandmarks.contains("Left Wrist")) {
            double leftElbowAngle = calculateAngle(leftShoulder, leftElbow, leftWrist);
            sb.append("- Left elbow angle: ").append(String.format("%.1f", leftElbowAngle)).append(" degrees\n");
        }

        if (importantLandmarks.contains("Right Shoulder") && importantLandmarks.contains("Right Elbow") && importantLandmarks.contains("Right Wrist")) {
            double rightElbowAngle = calculateAngle(rightShoulder, rightElbow, rightWrist);
            sb.append("- Right elbow angle: ").append(String.format("%.1f", rightElbowAngle)).append(" degrees\n");
        }

        if (importantLandmarks.contains("Left Hip") && importantLandmarks.contains("Left Knee") && importantLandmarks.contains("Left Ankle")) {
            double leftKneeAngle = calculateAngle(leftHip, leftKnee, leftAnkle);
            sb.append("- Left knee bend: ").append(String.format("%.1f", 180 - leftKneeAngle)).append(" degrees\n");
        }

        if (importantLandmarks.contains("Right Hip") && importantLandmarks.contains("Right Knee") && importantLandmarks.contains("Right Ankle")) {
            double rightKneeAngle = calculateAngle(rightHip, rightKnee, rightAnkle);
            sb.append("- Right knee bend: ").append(String.format("%.1f", 180 - rightKneeAngle)).append(" degrees\n");
        }

        return sb.toString();
    }
}
