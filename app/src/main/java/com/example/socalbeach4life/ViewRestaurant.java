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


public class ViewRestaurant extends AppCompatActivity {

    private FirebaseFirestore db;
    public User currUser;

    private OkHttpClient client;

    private boolean testing = false;

    public Restaurant testRestaurant;

    //public ViewRestaurant() throws IOException {db = FirebaseFirestore.getInstance();}

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client =  new OkHttpClient();
        Intent intent = getIntent();
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

            String[] menu = {"No menu available!"};

            String[] id = {""};

            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "Bearer " + key)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                public void onResponse(Call call, Response response)
                        throws IOException {
                    ResponseBody rb = response.body();
                    try {
                        JSONObject Jobject = new JSONObject(rb.string());
                        JSONArray Jarray = Jobject.getJSONArray("businesses");

                        if (Jarray.length() > 0){
                            JSONObject object  = Jarray.getJSONObject(0);
                            String alias = (String) object.get("alias");
                            id[0] = (String) object.get("id");
                            menu[0] = "yelp.com/menu/" + alias;
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }
            });

            sleep(1000);

            String[] startHour = new String[1];
            String[] endHour = new String[1];

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

            sleep(1000);

            TextView tv = new TextView(ViewRestaurant.this);
            if (startHour[0] == null){
                startHour[0] = "N/A";
            }
            if (endHour[0] == null){
                endHour[0] = "N/A";
            }
            String text = "Menu: " + menu[0]
                    + "\nStarting Hour: " + startHour[0]
                    + "\nEnding Hour: " + endHour[0];
            tv.setText(text);
            tv.setGravity(Gravity.CENTER);
            tv.setBackgroundColor(Color.WHITE);
            tv.setPadding(5, 5, 5, 5);

            GridLayout.LayoutParams title_layout = new GridLayout.LayoutParams();
            title_layout.setGravity(Gravity.CENTER_HORIZONTAL);
            grid.addView(tv, title_layout);
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
