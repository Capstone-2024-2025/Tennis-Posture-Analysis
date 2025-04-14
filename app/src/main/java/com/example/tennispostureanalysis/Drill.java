package com.example.tennispostureanalysis;

public class Drill {
    private String title;
    private String youtubeUrl;

    public Drill(String title, String youtubeUrl) {
        this.title = title;
        this.youtubeUrl = youtubeUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public String getThumbnailUrl() {
        // YouTube thumbnail format
        String videoId = extractYoutubeId(youtubeUrl);
        return "https://img.youtube.com/vi/" + videoId + "/0.jpg";
    }

    // This supports both short youtu.be links and standard youtube.com/watch?v= links.
    private String extractYoutubeId(String url) {
        try {
            // Handle shortened youtu.be links like: https://youtu.be/abc123
            if (url.contains("youtu.be/")) {
                // Get everything after the last '/' which is the video ID
                return url.substring(url.lastIndexOf("/") + 1);
            }

            // Handle standard YouTube links like: https://www.youtube.com/watch?v=abc123
            else if (url.contains("v=")) {
                // Split the string on "v=" and take the second part
                String[] parts = url.split("v=");
                String id = parts[1];

                // If there are additional parameters after the video ID (e.g., &t=1m32s),
                // we only want the video ID portion
                int ampersandPosition = id.indexOf("&");
                if (ampersandPosition != -1) {
                    // Return only the video ID (up to the first "&")
                    return id.substring(0, ampersandPosition);
                } else {
                    // No extra parameters; return the full ID
                    return id;
                }
            }
        } catch (Exception e) {
            // Print any unexpected errors (e.g., null URL or malformed format)
            e.printStackTrace();
        }

        // If the URL format isn't recognized, return an empty string to avoid crashes
        return "";
    }
}
