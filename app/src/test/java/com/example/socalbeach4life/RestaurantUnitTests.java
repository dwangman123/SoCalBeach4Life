package com.example.socalbeach4life;

import static org.junit.Assert.assertEquals;

import android.content.Intent;
import android.view.View;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.LooperMode;

@RunWith(RobolectricTestRunner.class)
public class RestaurantUnitTests {
    @Test
    public void testCreateRestaurant(){
        Restaurant restaurant = new Restaurant(10, 15, "test", 20, 25);

        assertEquals(restaurant.getLat(), 10, 0);
        assertEquals(restaurant.getLong(), 15, 0);
        assertEquals(restaurant.getName(), "test");
        assertEquals(restaurant.getBeachLat(), 20, 0);
        assertEquals(restaurant.getBeachlng(), 25, 0);
    }

    @Test
    public void testPinRestaurants(){
        try (ActivityController<MapsActivity> controller = Robolectric.buildActivity(MapsActivity.class, new Intent().putExtra("testing", true))) {
            controller.setup();
            MapsActivity activity = controller.get();

            activity.pinRestaurants();
            assertEquals(activity.restaurantMarkers.size(), 1);
        }
    }

    @Test
    public void testSetRadius(){
        try (ActivityController<MapsActivity> controller = Robolectric.buildActivity(MapsActivity.class, new Intent().putExtra("testing", true))) {
            controller.setup();
            MapsActivity activity = controller.get();

            activity.set2000(new View(activity.getApplicationContext()));
            assertEquals(activity.getRadius(), 2000);
        }
    }




}
