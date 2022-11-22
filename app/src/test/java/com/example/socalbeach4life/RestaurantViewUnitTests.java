package com.example.socalbeach4life;

import static org.junit.Assert.assertEquals;

import android.content.Intent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;

@RunWith(RobolectricTestRunner.class)
public class RestaurantViewUnitTests {
    @Test
    public void testInsertInfo(){
        try (ActivityController<ViewTrips> controller = Robolectric.buildActivity(ViewTrips.class, new Intent().putExtra("testing", true))) {
            controller.setup();
            ViewTrips activity = controller.get();
            activity.insertTrips();

            assertEquals(activity.trip, "Test trip");
        }
        try (ActivityController<ViewRestaurant> controller = Robolectric.buildActivity(ViewRestaurant.class, new Intent().putExtra("testing", true))) {
            controller.setup();
            ViewRestaurant activity = controller.get();
            activity.insertInformation(0, 0 , "test rest");

            assertEquals(activity.testRestaurant.getName(), "test rest");
        }
    }

    @Test
    public void testUserCreated(){
        try (ActivityController<ViewRestaurant> controller = Robolectric.buildActivity(ViewRestaurant.class, new Intent().putExtra("testing", true))) {
            controller.setup();
            ViewRestaurant activity = controller.get();

            assertEquals(activity.currUser.getName(), "Test");
        }
    }
}
