package com.example.socalbeach4life;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class ViewReviewsActivity extends AppCompatActivity {
    private String id;
    private String name;
    private FirebaseFirestore db;
    private boolean testing = false;
    public int testReviewCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reviews);

        Intent intent = getIntent();
        this.id = intent.getStringExtra("id");
        this.name = intent.getStringExtra("beachName");

        this.testing = intent.getBooleanExtra("testing", false);
        if (!testing && id != null) {
            this.db = FirebaseFirestore.getInstance();
            TextView nameView = (TextView) findViewById(R.id.beachNameView);
            nameView.setText(name);

            DocumentReference reviewDoc = this.db.collection("reviews").document(this.name);
            reviewDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot document = task.getResult();
                    ReviewWrapper wrapper;
                    if (task.isSuccessful()) {
                        if (document.exists()) {
                            wrapper = document.toObject(ReviewWrapper.class);
                            fillScreen(wrapper);
                        }
                    }
                }
            });
        } else {
            testReviewCount = 0;
        }

    }

    private void fillScreen(ReviewWrapper wrapper) {
        LinearLayout userReviewLayout = (LinearLayout) findViewById(R.id.userReviews);
        LinearLayout otherReviewLayout = (LinearLayout) findViewById(R.id.otherReviews);
        ArrayList<Review> reviews = wrapper.getReviewArrayList();
        for (int i=0; i<reviews.size(); i++) {
            LinearLayout parent = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            parent.setLayoutParams(params);
            parent.setOrientation(LinearLayout.VERTICAL);

            TextView userNameView = new TextView(this);
            TextView descriptionView = new TextView(this);
            LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            viewParams.setMargins(5, 0, 0, 5);

            userNameView.setLayoutParams(viewParams);
            descriptionView.setLayoutParams(viewParams);

            Review tempReview = reviews.get(i);

            String userName = tempReview.getAnonymous() ? "Anonymous" : tempReview.getUserName();
            String description = reviews.get(i).getDescription();
            float rating = reviews.get(i).getRating();

            userNameView.setText(userName);
            descriptionView.setText(description);

            RatingBar ratingBar = new RatingBar(this, null, androidx.appcompat.R.attr.ratingBarStyleSmall);
            LinearLayout.LayoutParams ratingBarParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            ratingBarParams.height = 60;
            ratingBar.setLayoutParams(ratingBarParams);

            ratingBar.setMax(5);
            ratingBar.setStepSize(0.5F);
            ratingBar.setIsIndicator(true);
            ratingBar.setNumStars(5);
            ratingBar.setRating(rating);

            parent.addView(userNameView);
            parent.addView(descriptionView);
            parent.addView(ratingBar);

            parent.setBackgroundResource(R.drawable.review_box_border);

            if (Objects.equals(tempReview.getUserId(), this.id)) {
                Button deleteButton = new Button(this);
                LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                deleteButton.setLayoutParams(buttonParams);
                deleteButton.setText("Delete");
                deleteButton.setTextSize(15);
                deleteButton.setBackgroundColor(Color.RED);
                deleteButton.setTextColor(Color.WHITE);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                         deleteReview(wrapper, tempReview);
                    }
                });

                parent.addView(deleteButton);

                userReviewLayout.addView(parent);
            }
            else
            {
                otherReviewLayout.addView(parent);
            }
        }
    }

    public void deleteReview(ReviewWrapper wrapper, Review review) {
        ReviewWrapper tempWrapper = wrapper;
        ArrayList<Review> reviews = wrapper.getReviewArrayList();
        reviews.remove(review);

        tempWrapper.setReviewArrayList(reviews);
        if (!testing) {
            this.db.collection("reviews").document(name).set(tempWrapper).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    refresh();
                }
            });
        } else {
            this.testReviewCount = tempWrapper.getReviewCount();
        }
    }

    private void refresh() {
        Intent intent = new Intent(this, ViewReviewsActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("beachName", name);
        startActivity(intent);
    }

    public void backToReviews(View view) {
        Intent intent = new Intent(this, ReviewMainActivity.class);
        intent.putExtra("id", this.id);
        startActivity(intent);
    }

    public void addToReviews(View view) {
        Intent intent = new Intent(this, AddReviewActivity.class);
        intent.putExtra("id", this.id);
        intent.putExtra("beachName", this.name);
        startActivity(intent);
    }
}