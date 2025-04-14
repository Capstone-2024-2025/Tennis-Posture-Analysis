package com.example.tennispostureanalysis;

import java.io.Serializable;

public class Session implements Serializable {
    private String title;
    private String thumbnailPath;

    public Session(String title, String thumbnailPath) {
        this.title = title;
        this.thumbnailPath = thumbnailPath;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }
}
