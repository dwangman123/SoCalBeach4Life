package com.example.socalbeach4life;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;

import java.util.ArrayList;
import java.util.Locale;

/**
 * An activity that displays a map showing the place at the device's current location.
 */
public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private ArrayList<BeachLocation> parkingLots;

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
            if(!marker.getTitle().substring(marker.getTitle().length() - 5).toLowerCase().equals("beach")){
                Button showParkingLots = ((Button)beachContentView.findViewById(R.id.showParkingLots));
                showParkingLots.setVisibility(View.GONE);
            }
            return beachContentView;
        }

        @Override
        public View getInfoContents(@NonNull Marker marker) {
            return null;
        }
    }

    private GoogleMap map;

    private final FirebaseFirestore db;
    private ArrayList<Beach> allBeaches;
    private User currUser;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private final LatLng defaultLocation = new LatLng(34.0083, -118.4988);
    private static final int DEFAULT_ZOOM = 10;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;

    private Location lastKnownLocation;

    public MapsActivity() {
        this.db = FirebaseFirestore.getInstance();
        allBeaches = new ArrayList<>();
        parkingLots = new ArrayList<>();
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
                        Toast.makeText(MapsActivity.this, "User loaded.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MapsActivity.this, "Error getting user info.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MapsActivity.this, "Error getting user info.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        // Prompt the user for permission.
        getLocationPermission();

        while(!locationPermissionGranted){
            try {
                Thread.sleep(10);
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
    private void updateLocationUI() {
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
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            System.out.println(e.getMessage());
        }
    }

    public void pinBeaches() {
        for (Beach b : allBeaches) {
            LatLng coords = new LatLng(b.getLat(), b.getLong());
            map.addMarker(new MarkerOptions().position(coords).title(b.getName()).snippet(b.getHours()));
        }
    }

    public void pinParkingLots(){
        for(BeachLocation l: parkingLots){
            LatLng coords = new LatLng(l.getLat(), l.getLong());
            map.addMarker(new MarkerOptions().position(coords).title(l.getName()));
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if(marker.getTitle().substring(marker.getTitle().length() - 5).toLowerCase().equals("beach")) {
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
}