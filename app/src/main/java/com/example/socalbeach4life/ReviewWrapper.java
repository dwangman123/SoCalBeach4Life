package com.example.socalbeach4life;


import androidx.annotation.NonNull;

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
        this.reviewArrayList = new ArrayList<Review>();
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
        this.rating = 0;
        for (int i = 0; i< reviewArrayList.size(); i++) {
            this.rating += reviewArrayList.get(i).getRating();
        }
        this.reviewCount = this.reviewArrayList.size();
        this.rating = this.rating/this.reviewCount;
    }

    public void setReviews(@NonNull ArrayList<Map<String, Object>> fromDb) {
        for (int i =0; i< fromDb.size(); i++) {
            Map<String, Object> tempReview = fromDb.get(i);
            this.rating += (float) tempReview.get("rating");
            addToReviews(new Review((float)tempReview.get("rating"), (String)tempReview.get("description"), (String)tempReview.get("userId"), (boolean)tempReview.get("anonymous"), (String)tempReview.get("userName")));
        }

        this.reviewCount = this.reviewArrayList.size();
        this.rating = this.rating/this.reviewCount;
    }

    public void setReviewArrayList(ArrayList<Review> newReviews){
        this.reviewArrayList = newReviews;
        this.rating = 0;
        for (int i = 0; i< reviewArrayList.size(); i++) {
            this.rating += reviewArrayList.get(i).getRating();
        }
        this.reviewCount = this.reviewArrayList.size();
        this.rating = this.rating/this.reviewCount;
    }
}
