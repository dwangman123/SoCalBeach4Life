package com.example.socalbeach4life;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.Distribution;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReviewMainActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private ArrayList<Beach> allBeaches;
    private static String id;
    private Map<String, ReviewWrapper> ratingMap = new HashMap<String, ReviewWrapper>();
    private boolean testing = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_main);
        Intent intent = getIntent();
        if(intent.hasExtra("testing")){
            this.testing=true;
            this.db = null;
            this.allBeaches = new ArrayList<Beach>();
            this.allBeaches.add(new Beach("Test", "None", 20, 20));
            fillScreen();
        } else {
            db = FirebaseFirestore.getInstance();
            this.allBeaches = MapsActivity.getAllBeaches();
            this.id = intent.getStringExtra("id");
            this.db.collection("reviews").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ratingMap.put(document.getId(), document.toObject(ReviewWrapper.class));
                        }
                        fillScreen();
                    }
                }
            });
        }
    }
    public void backToMap(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        if (this.id != null) {
            intent.putExtra("id", this.id);
        }
        startActivity(intent);
    }
    private void fillScreen() {
        for (int i = 0; i< allBeaches.size(); i++) {
            String name = allBeaches.get(i).getName();
            if (!ratingMap.containsKey(name)) {
                ratingMap.put(name, new ReviewWrapper(name));
            }
        }
        LinearLayout scrollView = (LinearLayout) findViewById(R.id.reviewsScrollView);

        for (String name: ratingMap.keySet()) {
            LinearLayout parent = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.height = 300;
            parent.setLayoutParams(params);
            parent.setOrientation(LinearLayout.VERTICAL);

            TextView beachName = new TextView(this);
            TextView reviewCountView = new TextView(this);
            LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            viewParams.height = 60;
            viewParams.setMargins(5, 0, 0, 5);

            beachName.setLayoutParams(viewParams);
            reviewCountView.setLayoutParams(viewParams);

            int count = ratingMap.get(name).getReviewCount();
            float rating = ratingMap.get(name).getRating();

            beachName.setText(name);
            reviewCountView.setText(count + " Reviews");

            RatingBar ratingBar = new RatingBar(this);
            LinearLayout.LayoutParams ratingBarParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            ratingBarParams.height = 120;
            ratingBar.setLayoutParams(ratingBarParams);

            ratingBar.setMax(5);
            ratingBar.setStepSize(0.5F);
            ratingBar.setIsIndicator(true);
            ratingBar.setNumStars(5);
            ratingBar.setRating(rating);

            parent.addView(beachName);
            parent.addView(reviewCountView);
            parent.addView(ratingBar);

            parent.setBackgroundResource(R.drawable.review_box_border);

            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), ViewReviewsActivity.class);
                    intent.putExtra("id", ReviewMainActivity.id);
                    intent.putExtra("beachName", ((TextView)parent.getChildAt(0)).getText());
                    startActivity(intent);
                }
            });

            scrollView.addView(parent);
        }

    }
}