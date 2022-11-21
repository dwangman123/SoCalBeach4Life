package com.example.socalbeach4life;

import static java.lang.Thread.sleep;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * An activity that displays a map showing the place at the device's current location.
 */
public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener {

    private ArrayList<BeachLocation> parkingLots;
    private ArrayList<Restaurant> allRestaurants;

    class BeachWindow implements GoogleMap.InfoWindowAdapter{
        private final View beachContentView;

        BeachWindow(){
            beachContentView = getLayoutInflater().inflate(R.layout.beach_marker, null);
        }

        @Override
        public View getInfoWindow(@NonNull Marker marker) {
            TextView tvTitle = ((TextView)beachContentView.findViewById(R.id.name));
            tvTitle.setText(marker.getTitle());
            TextView tvSnippet = ((TextView)beachContentView.findViewById(R.id.hours));
            tvSnippet.setText(marker.getSnippet());
            Button showParkingLots = ((Button)beachContentView.findViewById(R.id.showParkingLots));
            String snip = marker.getSnippet();
            if(snip.equals("Route")){
                showParkingLots.setText("End route");
            } else if(snip.equals("Parking lot")){
                showParkingLots.setText("Route to parking lot");
            } else if (snip.equals("Restaurant")) {
                showParkingLots.setText("Menu and Hours");
            }else{
                showParkingLots.setText("Show Parking Lots");
            }

            return beachContentView;
        }

        @Override
        public View getInfoContents(@NonNull Marker marker) {
            return null;
        }
    }

    public GoogleMap map;
    public ArrayList<MarkerOptions> beachMarkers;

    private FirebaseFirestore db;
    private ArrayList<Beach> allBeaches;
    private static ArrayList<Beach> staticAllBeaches;
    private User currUser;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private final LatLng defaultLocation = new LatLng(34.0083, -118.4988);
    private static final int DEFAULT_ZOOM = 10;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    public boolean locationPermissionGranted;

    private Polyline currentRoute;
    private String source;
    private String destination;
    private LocalDateTime tripStart;
    private Marker tempMarker;

    private int currentRadius = 1000;

    public ArrayList<Marker> currentRestaurants;
    private Circle currentCircle;

    private Location lastKnownLocation;

    private boolean testing;

    public MapsActivity() {
        allBeaches = new ArrayList<>();
        staticAllBeaches = new ArrayList<Beach>();
        parkingLots = new ArrayList<>();
        allRestaurants = new ArrayList<>();
        currentRestaurants = new ArrayList<>();
        beachMarkers = new ArrayList<>();
        testing = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if(intent.hasExtra("testing")){
            testing = true;
            db = null;
            allRestaurants.add(new Restaurant(0, 0, "Test", 0, 0));
            allBeaches.add(new Beach("Test", "None", 0, 0));
        } else {
            db = FirebaseFirestore.getInstance();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if(!testing) {
            String id = intent.getStringExtra("id");
            DocumentReference userDoc = this.db.collection("users").document(id);
            userDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            currUser = document.toObject(User.class);
                            Toast.makeText(MapsActivity.this, "User loaded.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MapsActivity.this, "Error getting user info.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MapsActivity.this, "Error getting user info.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;

        map.setInfoWindowAdapter(new BeachWindow());
        map.setOnInfoWindowClickListener(this);
        map.setOnMarkerClickListener(this);
        if(!testing) {

            // Prompt the user for permission.
            getLocationPermission();

            while(!locationPermissionGranted){
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

            // Turn on the My Location layer and the related control on the map.
            updateLocationUI();

            // Get the current location of the device and set the position of the map.
            getDeviceLocation();
        }
    }

    public void goToTrips(View view){
        Intent intent = new Intent(this, ViewTrips.class);
        intent.putExtra("id", currUser.getId());
        this.startActivity(intent);
    }

    public void goToReviews(View view) {
        Intent intent = new Intent(this, ReviewMainActivity.class);
        intent.putExtra("id", currUser.getId());
        this.startActivity(intent);
    }

    public void goUserPage(View view) {
        Intent intent = new Intent(this, LogoutActivity.class);
        intent.putExtra("id", currUser.getId());
        this.startActivity(intent);
    }

    public void set1000(View view){
        currentRadius = 1000;
    }

    public void set2000(View view){
        currentRadius = 2000;
    }

    public void set3000(View view){
        currentRadius = 3000;
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            }
                            // Load beaches if not loaded
                            if(allBeaches.size() == 0){
                                while(lastKnownLocation == null) {
                                    try{
                                        sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                PlacesSearchResponse request = new NearbySearch().run(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), 40000, "beach", PlaceType.TOURIST_ATTRACTION, getString(R.string.google_maps_key));
                                for(int i = 0; i<request.results.length && allBeaches.size() < 5; i++){
                                    if(request.results[i].name.length() > 5 && request.results[i].name.substring(request.results[i].name.length() - 5).toLowerCase().equals("beach")) {
                                        Beach b = new Beach(request.results[i].name,
                                                            request.results[i].openingHours != null && request.results[i].openingHours.weekdayText != null ? request.results[i].openingHours.weekdayText[0] : "No hours listed",
                                                            request.results[i].geometry.location.lat,
                                                            request.results[i].geometry.location.lng);
                                        allBeaches.add(b);
                                    }
                                }
                                staticAllBeaches = allBeaches;
                                pinBeaches();
                            }
                        } else {
                            map.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            map.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        if (requestCode
                == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        updateLocationUI();
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    public void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                if(!testing) {
                    getLocationPermission();
                }
            }
        } catch (SecurityException e)  {
            System.out.println(e.getMessage());
        }
    }

    public void pinBeaches() {
        for (Beach b : allBeaches) {
            LatLng coords = new LatLng(b.getLat(), b.getLong());
            if(!testing) {
                map.addMarker(new MarkerOptions().position(coords).title(b.getName()).snippet(b.getHours()));
            }
            beachMarkers.add(new MarkerOptions().position(coords).title(b.getName()).snippet(b.getHours()));
        }
    }

    public void pinParkingLots(){
        for(BeachLocation l: parkingLots){
            LatLng coords = new LatLng(l.getLat(), l.getLong());
            map.addMarker(new MarkerOptions().position(coords).title(l.getName()).snippet("Parking lot").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        }
    }

    public void pinRestaurants(){
        for (Restaurant r: allRestaurants){
            LatLng coords = new LatLng(r.getLat(), r.getLong());
            Marker marker = map.addMarker(new MarkerOptions().position(coords).title(r.getName()).snippet("Restaurant").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            currentRestaurants.add(marker);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker){
        if (!marker.getSnippet().equals("Parking lot") && !marker.getSnippet().equals("Route") && !marker.getSnippet().equals("Restaurant")){

            allRestaurants.clear();

            for (Marker m : currentRestaurants) {
                m.remove();
            }

            if (currentCircle != null) {
                currentCircle.remove();
            }


            double lat = 0, lng = 0;
            for (int i = 0; i < allBeaches.size(); i++) {
                if (allBeaches.get(i).getName().equals(marker.getTitle())) {
                    lat = allBeaches.get(i).getLat();
                    lng = allBeaches.get(i).getLong();
                }
            }

            CircleOptions circleOptions = new CircleOptions();
            LatLng ll = new LatLng(lat, lng);
            circleOptions.center(ll);
            circleOptions.radius(currentRadius);
            circleOptions.fillColor(0x30ff0000);

            if(!testing) {
                currentCircle = map.addCircle(circleOptions);


                PlacesSearchResponse request = new NearbySearch().run(lat, lng, currentRadius, "", PlaceType.RESTAURANT, getString(R.string.google_maps_key));
                for (int i = 0; i < request.results.length && allRestaurants.size() < 6; i++) {
                    Restaurant r = new Restaurant(request.results[i].geometry.location.lat, request.results[i].geometry.location.lng, request.results[i].name, lat, lng);
                    allRestaurants.add(r);
                }
                pinRestaurants();
            }
        } else if(marker.getSnippet().equals("Restaurant")){
            if(currentRoute != null){
                currentRoute.remove();
                currentRoute = null;
                tempMarker.remove();
                tempMarker = null;
            }
            Restaurant res = null;
            for(Restaurant r: allRestaurants){
                if(r.getName().equals(marker.getTitle())){
                    res = r;
                }
            }
            if(!testing) {
                String url = getDirectionsURL(res.getBeachLat(), res.getBeachlng(), res.getLat(), res.getLong(), "walking");
                FetchUrl FetchUrl = new FetchUrl();
                FetchUrl.execute(url);
                tripStart = LocalDateTime.now();
            }
        }
        return false;
    }

    public static ArrayList<Beach> getAllBeaches() {
        return staticAllBeaches;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if(marker.getSnippet().equals("Parking lot")){
            BeachLocation lot = null;
            for(BeachLocation b: parkingLots){
                if(b.getName().equals(marker.getTitle())){
                    lot = b;
                }
            }
            String url = getDirectionsURL(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), lot.getLat(), lot.getLong(), "driving");
            FetchUrl FetchUrl = new FetchUrl();
            FetchUrl.execute(url);
            tripStart = LocalDateTime.now();
        } else if(marker.getSnippet().equals("Route")){
            currentRoute.remove();
            tempMarker.remove();
            Trip currentTrip = new Trip(tripStart, LocalDateTime.now(), source, destination, currUser);
            currentTrip.upload();
        } else if (marker.getSnippet().equals("Restaurant")) {
            Intent intent = new Intent(this, ViewRestaurant.class);
            intent.putExtra("id", currUser.getId());
            double lat = 0, lng = 0;
            String name = "";
            for (int i = 0; i < allRestaurants.size(); i++) {
                if (allRestaurants.get(i).getName().equals(marker.getTitle())) {
                    lat = allRestaurants.get(i).getLat();
                    lng = allRestaurants.get(i).getLong();
                    name = allRestaurants.get(i).getName();
                }
            }

            intent.putExtra("lat", lat);
            intent.putExtra("lng", lng);
            intent.putExtra("name", name);
            this.startActivity(intent);
        } else {
            parkingLots.clear();
            double lat = 0, lng = 0;
            for (int i = 0; i < allBeaches.size(); i++) {
                if (allBeaches.get(i).getName().equals(marker.getTitle())) {
                    lat = allBeaches.get(i).getLat();
                    lng = allBeaches.get(i).getLong();
                }
            }
            PlacesSearchResponse request = new NearbySearch().run(lat, lng, 1000, "parking", PlaceType.PARKING, getString(R.string.google_maps_key));
            for (int i = 0; i < request.results.length && parkingLots.size() < 3; i++) {
                BeachLocation park = new BeachLocation(request.results[i].geometry.location.lat, request.results[i].geometry.location.lng, request.results[i].name);
                parkingLots.add(park);
            }
            pinParkingLots();
        }
    }

    public String getDirectionsURL(double curLat, double curLng, double destLat, double destLng, String modeName) {
        String str_origin = "origin=" + curLat + "," + curLng;
        String str_dest = "destination=" + destLat + "," + destLng;
        String sensor = "sensor=false";
        String mode = "mode=" + modeName;

        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode + "&key=" + (testing ? "" : getString(R.string.google_maps_key));

        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }


    private class FetchUrl extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String[] url) {
            String data = "";
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }

        private String downloadUrl(String strUrl) throws IOException {
            String data = "";
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(strUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                iStream = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
                StringBuffer sb = new StringBuffer();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                data = sb.toString();
                br.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                iStream.close();
                urlConnection.disconnect();
            }
            return data;
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String[] jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jObject = new JSONObject(jsonData[0]);
                DataParser parser = new DataParser();
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = result.get(i);
                for (int j = 0; j < path.size() - 1; j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);
                tempMarker = map.addMarker(new MarkerOptions()
                        .title(path.get(path.size()-1).get("time"))
                        .position(new LatLng(Double.parseDouble(path.get(path.size()-1).get("startLat")), Double.parseDouble(path.get(path.size()-1).get("startLng"))))
                        .snippet("Route")
                        .visible(true));
                source = path.get(path.size()-1).get("source");
                destination = path.get(path.size()-1).get("dest");
                boolean isBeach = false;
                for(Beach b: allBeaches){
                    float[] results = new float[1];
                    Location.distanceBetween(Double.parseDouble(path.get(path.size()-1).get("startLat")), Double.parseDouble(path.get(path.size()-1).get("startLng")), b.getLat(), b.getLong(), results);
                    if(results[0] < 150){
                        isBeach = true;
                    }
                }
                if(!isBeach){
                    tempMarker.showInfoWindow();
                }
            }
            if(lineOptions != null) {
                currentRoute = map.addPolyline(lineOptions);
            }
        }
    }
}