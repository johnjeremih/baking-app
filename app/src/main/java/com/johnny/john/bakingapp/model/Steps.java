package com.johnny.john.bakingapp.model;

import java.io.Serializable;

public class Steps implements Serializable {


    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;


    public Steps(String shortDescription, String description, String videoURL, String thumbnailURL){
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }

    public String getShortDescription() {
        return shortDescription;
    }
    public String getDescription() {
        return description;
    }
    public String getVideoURL() {
        return videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }
}
