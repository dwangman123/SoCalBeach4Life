package com.example.socalbeach4life;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Authorization {
    private Context StartActivity;
    private FirebaseFirestore db;

    public Authorization(Context context) {
        StartActivity = context;
        this.db = FirebaseFirestore.getInstance();
    }

    public void createUser(String name, String email, String phone, String password) {
        Map<String, String> authUser = new HashMap<>();
        authUser.put("password", password);
        String userUuid = UUID.randomUUID().toString();
        authUser.put("id", userUuid);
        // persist auth obj to authentication collection - auth obj keyed by username(email)
        this.db.collection("auth").document(email).set(authUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    persistToUsersAndSwitch(name, email, phone, userUuid);
                } else {
                    Toast.makeText(StartActivity, "Registration failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void persistToUsersAndSwitch(String name, String email, String phone, String id){
        // persist user obj to user collection
        User user = new User(email, name, phone, id);
        // user object keyed by uuid
        this.db.collection("users").document(id).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(StartActivity, "Added user...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(StartActivity, MapsActivity.class);
                    intent.putExtra("id", id);
                    StartActivity.startActivity(intent);
                } else {
                    Toast.makeText(StartActivity, "User addition failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void login(String email, String password) {
        Toast.makeText(StartActivity, "Entered Auth Login...", Toast.LENGTH_SHORT).show();
        DocumentReference authUser = this.db.collection("auth").document(email);
        authUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        Map<String, Object> passwordAndId = document.getData();
                        if (password.equals(passwordAndId.get("password"))) {
                            Intent intent = new Intent(StartActivity, MapsActivity.class);
                            intent.putExtra("id", (String) passwordAndId.get("id"));
                            StartActivity.startActivity(intent);
                        } else {
                            Toast.makeText(StartActivity, "Password is wrong.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(StartActivity, "No such user exists.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
