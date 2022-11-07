package com.example.socalbeach4life;

import android.media.Image;

import java.util.ArrayList;
import java.util.Map;

public class ReviewWrapper {
    private String beachName;
    private float rating;
    private int reviewCount;
    private ArrayList<Review> reviewArrayList;

    public ReviewWrapper(){}
    public ReviewWrapper(String beachName) {
        this.beachName = beachName;
        this.rating = 0;
        this.reviewCount = 0;
    }

    public String getBeachName() {
        return beachName;
    }

    public float getRating() {
        return rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public ArrayList<Review> getReviewArrayList() {
        return reviewArrayList;
    }

    public void addToReviews(Review newReview) {
        reviewArrayList.add(newReview);
    }

    public void setReviews(ArrayList<Map<String, Object>> fromDb) {
        for (int i =0; i< fromDb.size(); i++) {
            Map<String, Object> tempReview = fromDb.get(i);
            this.rating += (float) tempReview.get("rating");
            addToReviews(new Review((float)tempReview.get("rating"), (String)tempReview.get("description"), (Image)tempReview.get("picture"), (String)tempReview.get("userId")));
        }

        this.reviewCount = this.reviewArrayList.size();
        this.rating = this.rating/this.reviewCount;
    }
}
