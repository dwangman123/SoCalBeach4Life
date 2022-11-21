package com.example.socalbeach4life;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Trip {
    private LocalDateTime start;
    private LocalDateTime end;
    private String source;
    private String dest;
    private User user;
    private FirebaseFirestore db;
    public boolean testing = false;
    public Map<String, String> tripData;
    public Trip(LocalDateTime start, LocalDateTime end, String source, String dest, User user){
        this.start = start;
        this.end = end;
        this.source = source;
        this.dest = dest;
        this.user = user;
        db = FirebaseFirestore.getInstance();
    }

    public void upload(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        Map<String, String> tripData = new HashMap<>();
        tripData.put("start", dtf.format(start));
        tripData.put("end", dtf.format(end));
        tripData.put("source", source);
        tripData.put("dest", dest);
        if(!testing) {
            DocumentReference newTrip = db.collection("trips").document();
            newTrip.set(tripData);
            String tripId = newTrip.getId();
            DocumentReference trips = db.collection("users").document(user.getId());
            trips.update("trips", FieldValue.arrayUnion(tripId));
        } else {
            this.tripData = tripData;
        }
    }
}
