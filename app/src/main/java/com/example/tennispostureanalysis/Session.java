package com.example.tennispostureanalysis;

import java.io.Serializable;

public class Session implements Serializable {
    private String videoUri;
    private long timestamp;

    public Session(String videoUri, long timestamp) {
        this.videoUri = videoUri;
        this.timestamp = timestamp;
    }

    public String getVideoUri() {
        return videoUri;
    }

    public long getTimestamp() {
        return timestamp;
    }
}

