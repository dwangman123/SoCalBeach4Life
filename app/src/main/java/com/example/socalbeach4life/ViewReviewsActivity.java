package com.example.socalbeach4life;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ViewReviewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reviews);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("beachName");

        TextView nameView = (TextView) findViewById(R.id.nameView);
        TextView idView = (TextView) findViewById(R.id.idView);

        nameView.setText(name);
        idView.setText(id);
    }
}