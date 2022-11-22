package com.example.socalbeach4life;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.UUID;

public class AddReviewActivity extends AppCompatActivity {
    private String id;
    private String name;
    private String userName ="";
    private Review review;
    private FirebaseFirestore db;
    private boolean testing = false;
    public Review testReview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        this.id = intent.getStringExtra("id");
        this.name = intent.getStringExtra("beachName");
        if (intent.getBooleanExtra("testing", false)) {
            this.testing = true;
        }


        this.review = new Review();
        this.review.setId(this.id);
        if(!testing) {
            this.db = FirebaseFirestore.getInstance();
            DocumentReference userDoc = this.db.collection("users").document(id);
            userDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()){
                            User currUser = document.toObject(User.class);
                            userName = currUser.getName();

                            Toast.makeText(AddReviewActivity.this, "User loaded.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddReviewActivity.this, "Error getting user info.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(AddReviewActivity.this, "Error getting user info.", Toast.LENGTH_SHORT).show();
                    }
                    setContentView(R.layout.activity_add_review);
                    TextView nameView = (TextView) findViewById(R.id.beachNameView);
                    nameView.setText(name);
                    review.setUserName(userName);
                }
            });
        } else {
            this.review.setUserName("testUserName");
        }

    }

    public void backToReviewView(View view) {
        Intent intent = new Intent(this, ViewReviewsActivity.class);
        intent.putExtra("id", this.id);
        intent.putExtra("beachName", this.name);
        startActivity(intent);
    }
    public void backToReviewView() {
        Intent intent = new Intent(this, ViewReviewsActivity.class);
        intent.putExtra("id", this.id);
        intent.putExtra("beachName", this.name);
        startActivity(intent);
    }

    public void postReview(View view) {
        EditText ratingText = (EditText) findViewById(R.id.ratingEditText);
        EditText descriptionText = (EditText) findViewById(R.id.reviewDescription);
        CheckBox anonCheck = (CheckBox) findViewById(R.id.anonCheck);
        float rating = testing ? Float.parseFloat("5") : Float.parseFloat(ratingText.getText().toString());
        String desc = testing ? "testDescription" : descriptionText.getText().toString();
        boolean anon = testing ? false : anonCheck.isChecked();

        this.review.setRating(rating);
        this.review.setDescription(desc);
        this.review.setAnonymous(anon);
        this.review.setReviewId(UUID.randomUUID().toString());

        if (!testing) {
            DocumentReference reviewDoc = this.db.collection("reviews").document(this.name);
            reviewDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot document = task.getResult();
                    ReviewWrapper wrapper;
                    if (task.isSuccessful()) {
                        if (document.exists()) {
                            wrapper = document.toObject(ReviewWrapper.class);
                        } else {
                            wrapper = new ReviewWrapper(name);
                        }
                    } else {
                        wrapper = new ReviewWrapper(name);
                    }
                    wrapper.addToReviews(review);
                    db.collection("reviews").document(name).set(wrapper).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            backToReviewView();
                        }
                    });
                }
            });
        } else {
            this.testReview = this.review;
        }
    }

}