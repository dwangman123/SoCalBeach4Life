package com.example.socalbeach4life;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;

import static org.junit.Assert.*;

import android.content.Intent;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)
public class MapUnitTests {
    @Test
    public void testGetDirectionsURL(){
        try (ActivityController<MapsActivity> controller = Robolectric.buildActivity(MapsActivity.class, new Intent().putExtra("testing", true))) {
            controller.setup();
            MapsActivity activity = controller.get();

            String url = activity.getDirectionsURL(100, 100, 100, 100, "driving");
            assertEquals(url, "https://maps.googleapis.com/maps/api/directions/json?origin=100.0,100.0&destination=100.0,100.0&sensor=false&mode=driving&key=");
        }
    }
}