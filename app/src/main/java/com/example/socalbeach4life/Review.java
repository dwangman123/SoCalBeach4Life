package com.example.socalbeach4life;

import android.media.Image;

public class Review {
    private float rating;
    private String description = null;
    private Image picture = null;
    private String userId;

    public Review(float rating, String description, Image picture, String userId) {
        this.rating = rating;
        this.description = description;
        this.picture=picture;
        this.userId = userId;
    }

    public float getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }

    public Image getPicture() {
        return picture;
    }

    public String getUserId() {
        return userId;
    }
}
