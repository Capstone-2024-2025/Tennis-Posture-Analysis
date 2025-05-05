package com.example.tennispostureanalysis;

import com.google.mediapipe.formats.proto.LandmarkProto.NormalizedLandmarkList;
import java.util.HashMap;
import java.util.Map;

/**
 * Extracts key pose landmarks (shoulders, elbows, and hips) from the MediaPipe NormalizedLandmarkList.
 * Indices (per MediaPipe Pose documentation):
 *   - Left Shoulder: 11
 *   - Right Shoulder: 12
 *   - Left Elbow: 13
 *   - Right Elbow: 14
 *   - Left Hip: 23
 *   - Right Hip: 24
 */
public class PoseKeyPoints {
    public PoseLandMark leftShoulder, rightShoulder, leftElbow, rightElbow, leftHip, rightHip;

    public PoseKeyPoints(NormalizedLandmarkList landmarks) {
        // Ensure we have enough landmarks. MediaPipe Pose typically returns 33 landmarks.
        if (landmarks == null || landmarks.getLandmarkCount() < 25) {
            throw new IllegalArgumentException("Invalid landmarks list: Not enough landmarks.");
        }
        leftShoulder = createPoseLandMark(landmarks, 11);
        rightShoulder = createPoseLandMark(landmarks, 12);
        leftElbow = createPoseLandMark(landmarks, 13);
        rightElbow = createPoseLandMark(landmarks, 14);
        leftHip = createPoseLandMark(landmarks, 23);
        rightHip = createPoseLandMark(landmarks, 24);
    }

    private PoseLandMark createPoseLandMark(NormalizedLandmarkList landmarks, int index) {
        return new PoseLandMark(
                landmarks.getLandmark(index).getX(),
                landmarks.getLandmark(index).getY(),
                landmarks.getLandmark(index).getVisibility()
        );
    }

    /**
     * Converts the keypoints to a Map so they can be easily uploaded to Firebase.
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("leftShoulder", poseLandMarkToMap(leftShoulder));
        map.put("rightShoulder", poseLandMarkToMap(rightShoulder));
        map.put("leftElbow", poseLandMarkToMap(leftElbow));
        map.put("rightElbow", poseLandMarkToMap(rightElbow));
        map.put("leftHip", poseLandMarkToMap(leftHip));
        map.put("rightHip", poseLandMarkToMap(rightHip));
        return map;
    }

    private Map<String, Object> poseLandMarkToMap(PoseLandMark plm) {
        Map<String, Object> plmMap = new HashMap<>();
        plmMap.put("x", plm.getX());
        plmMap.put("y", plm.getY());
        plmMap.put("visibility", plm.getVisible());
        return plmMap;
    }
}
