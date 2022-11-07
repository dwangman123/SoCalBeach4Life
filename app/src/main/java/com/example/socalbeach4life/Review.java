package com.example.socalbeach4life;


import java.util.UUID;

public class Review {
    private float rating;
    private String description = null;
    private String userId;
    private String userName;
    private boolean anonymous;
    private String reviewId;

    public Review() {}

    public Review(float rating, String description, String userId, boolean anonymous, String userName) {
        this.rating = rating;
        this.description = description;
        this.userId = userId;
        this.anonymous = anonymous;
        this.userName = userName;
        this.reviewId = UUID.randomUUID().toString();
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public float getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }


    public String getUserId() {
        return userId;
    }

    public boolean getAnonymous() {
        return anonymous;
    }


    public void setId(String id) {
        this.userId = id;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }
}
