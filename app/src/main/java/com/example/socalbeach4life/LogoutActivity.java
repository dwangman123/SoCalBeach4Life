package com.example.socalbeach4life;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LogoutActivity extends AppCompatActivity {
    private User currUser;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.db = FirebaseFirestore.getInstance();

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
                        Toast.makeText(LogoutActivity.this, "User loaded.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LogoutActivity.this, "Error getting user info.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LogoutActivity.this, "Error getting user info.", Toast.LENGTH_SHORT).show();
                }
                setContentView(R.layout.activity_logout);
            }
        });

        TextView nameView = (TextView) findViewById(R.id.nameView);
        TextView emailView = (TextView) findViewById(R.id.emailView);
        TextView phoneView = (TextView) findViewById(R.id.phoneView);
        nameView.setText(currUser.getName());
        emailView.setText(currUser.getEmail());
        phoneView.setText(currUser.getPhoneNo());

    }

    public void logout(View view) {
        Intent intent = new Intent(this, StartActivity.class);
        this.startActivity(intent);
    }

    public void backToMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("id", currUser.getId());
        this.startActivity(intent);
    }
}