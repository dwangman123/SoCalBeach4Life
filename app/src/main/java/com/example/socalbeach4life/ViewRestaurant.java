package com.example.socalbeach4life;


import static android.os.SystemClock.sleep;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.util.Random;
import java.util.ArrayList;


public class ViewRestaurant extends AppCompatActivity {

    private FirebaseFirestore db;
    public User currUser;

    private OkHttpClient client;

    private boolean testing = false;

    public Restaurant testRestaurant;

    public ArrayList<String> menu;
    public ArrayList<Integer> items;
    public String[] startHour = new String[1];
    public String[] endHour = new String[1];

    private final String[] menuItems = {"Vodka Pasta", "Ribeye Steak", "Pepperoni Pizza", "Mashed Potatoes", "Mac & Cheese", "Fried Chicken", "Fried Rice", "Chicken Pot Pie", "Tater Tots", "Fish Taco", "Steak Burrito", "Cheese Quesadilla", "Cheeseburger", "Hamburger","Caesar Salad","Apple Pie","Grilled Cheese", "Hot Dog", "French Fries", "Chicken Tenders", "Vanilla Milkshake"};

    //public ViewRestaurant() throws IOException {db = FirebaseFirestore.getInstance();}

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client =  new OkHttpClient();
        Intent intent = getIntent();
        menu = new ArrayList<String>();
        items = new ArrayList<Integer>();
        if(intent.hasExtra("testing")){
            db = null;
            testing = true;
            currUser = new User("test", "Test", "test", "t");
            insertInformation(0, 0, "Test restaurant");
        } else {
            db = FirebaseFirestore.getInstance();
        }
        if(intent.hasExtra("id")) {
            String id = intent.getStringExtra("id");
            DocumentReference userDoc = this.db.collection("users").document(id);
            userDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            insertInformation(intent.getDoubleExtra("lat", 1), intent.getDoubleExtra("lng", 1), intent.getStringExtra("name"));
                            currUser = document.toObject(User.class);
                            Toast.makeText(ViewRestaurant.this, "User loaded.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ViewRestaurant.this, "Error getting user info.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ViewRestaurant.this, "Error getting user info.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }

    public void insertInformation(double lat, double lng, String name)  {
        setContentView(R.layout.activity_view_restaurant);
        GridLayout grid = (GridLayout) findViewById(R.id.trips);

        if (!testing){
            String url = "https://api.yelp.com/v3/businesses/search?latitude=" + lat + "&longitude=" + lng+"&term=" + name;
            String key = "-ny-9Blags4pZDTuOyMjLI9_GRyEmR1O9i3lzCq3iMv54Jce-QPm0ezJE2wgFPOwSh0WTUkuo9ufaF-4qTHkxiVgzWSgfE8_2r6-3_qXmA8CQHrlcIY_MpwzX8x6XHYx";

            String[] id = {""};

            Random rand = new Random();
            for (int i=0; i<5;i++){
                int n = rand.nextInt(20) + 1;
                while (items.contains(n)) {
                    n = rand.nextInt(20) + 1;
                }
                items.add(n);
            }

            for (int x: items){
                String s = String.valueOf(x);
                DocumentReference menuDoc = this.db.collection("menu").document(s);
                menuDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                menu.add((String)document.get("name"));
                                Toast.makeText(ViewRestaurant.this, "Item loaded.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ViewRestaurant.this, "Error getting menu info.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ViewRestaurant.this, "Error getting menu info.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }




            String url2 = "https://api.yelp.com/v3/businesses/" + id[0];
            Request request2 = new Request.Builder()
                    .url(url2)
                    .addHeader("Authorization", "Bearer " + key)
                    .build();

            Call call2 = client.newCall(request2);
            call2.enqueue(new Callback() {
                public void onResponse(Call call2, Response response2)
                        throws IOException {
                    ResponseBody rb = response2.body();
                    try {

                        JSONObject Jobject = new JSONObject(rb.string());
                        JSONArray Jarray = Jobject.getJSONArray("hours");
                        JSONObject object = Jarray.getJSONObject(0);
                        JSONArray objarr = object.getJSONArray("open");
                        System.out.println(objarr);
                        JSONObject hoursobj = objarr.getJSONObject(0);
                        startHour[0] = (String)hoursobj.get("start");
                        endHour[0] = (String) hoursobj.get("end");



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }
            });

            sleep(2000);

            TextView tv = new TextView(ViewRestaurant.this);
            if (startHour[0] == null){
                startHour[0] = "N/A";
            }
            if (endHour[0] == null){
                endHour[0] = "N/A";
            }

            String text = "Menu: ";
            for (int i=0;i<5;i++){
                int n = rand.nextInt(21);
                while (items.contains(n)) {
                    n = rand.nextInt(21);
                }
                items.add(n);
                text += menuItems[n] + "\n";
            }

            int[] startingHours = {6, 7, 8, 9, 10};
            int[] endingHours = {20, 21, 22, 23};
            int startingRand = rand.nextInt(5);
            int endingRand = rand.nextInt(4);

            text += "\nStarting Hour: " + startingHours[startingRand]
                    + "\nEnding Hour: " + endingHours[endingRand];
            tv.setText(text);
            tv.setGravity(Gravity.CENTER);
            tv.setBackgroundColor(Color.WHITE);
            tv.setPadding(5, 5, 5, 5);

            GridLayout.LayoutParams title_layout = new GridLayout.LayoutParams();
            title_layout.setGravity(Gravity.CENTER_HORIZONTAL);
            grid.addView(tv, title_layout);



//
        }else{
            testRestaurant = new Restaurant(lat, lng, name, 0 , 0);
            TextView tv = new TextView(ViewRestaurant.this);
            tv.setText(name);
            grid.addView(tv);
        }


    }

    public void backToMap(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("id", currUser.getId());
        startActivity(intent);
    }


}

