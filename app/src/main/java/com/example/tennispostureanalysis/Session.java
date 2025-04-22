package com.example.tennispostureanalysis;

import java.io.Serializable;

public class Session implements Serializable {
    private String thumbnailPath;
    private long timestamp;

    public Session(String thumbnailPath, long timestamp) {
        this.thumbnailPath = thumbnailPath;
        this.timestamp = timestamp;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
