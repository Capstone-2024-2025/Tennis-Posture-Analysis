package com.example.tennispostureanalysis;

import java.io.Serializable;

public class Session implements Serializable {
    private String thumbnailPath;
    private String videoPath;
    private long timestamp;

    public Session(String thumbnailPath, String videoPath, long timestamp) {
        this.thumbnailPath = thumbnailPath;
        this.videoPath = videoPath;
        this.timestamp = timestamp;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
