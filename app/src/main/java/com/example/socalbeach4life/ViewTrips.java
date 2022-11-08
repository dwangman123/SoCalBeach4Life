package com.example.socalbeach4life;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.LayoutDirection;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewTrips extends AppCompatActivity {

    private FirebaseFirestore db;
    private User currUser;

    public ViewTrips(){
        db = FirebaseFirestore.getInstance();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        DocumentReference userDoc = this.db.collection("users").document(id);
        userDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        currUser = document.toObject(User.class);
                        Toast.makeText(ViewTrips.this, "User loaded.", Toast.LENGTH_SHORT).show();
                        insertTrips();
                    } else {
                        Toast.makeText(ViewTrips.this, "Error getting user info.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ViewTrips.this, "Error getting user info.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void insertTrips(){
        setContentView(R.layout.activity_view_trips);
        GridLayout grid = (GridLayout) findViewById(R.id.trips);
        for(String trip: currUser.getTrips()){
            DocumentReference tripDoc = this.db.collection("trips").document(trip);
            tripDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task){
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            TextView tv = new TextView(ViewTrips.this);
                            String text = "Previous Trip:"
                                    + "\nFrom: " + document.getString("source")
                                    + "\nTo: " + document.getString("dest")
                                    + "\nStart time: " + document.getString("start")
                                    + "\nEnd time: " + document.getString("end");
                            tv.setText(text);
                            tv.setGravity(Gravity.CENTER);
                            tv.setBackgroundColor(Color.WHITE);
                            tv.setPadding(5, 5, 5, 5);

                            GridLayout.LayoutParams title_layout = new GridLayout.LayoutParams();
                            title_layout.setGravity(Gravity.CENTER_HORIZONTAL);
                            grid.addView(tv, title_layout);
                        }
                    }
                }
            });
        }
    }

    public void backToMap(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("id", currUser.getId());
        startActivity(intent);
    }
}