package com.example.socalbeach4life;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Intent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;

@RunWith(RobolectricTestRunner.class)
public class TripViewUnitTests {
    @Test
    public void testUserCreated(){
        try (ActivityController<ViewTrips> controller = Robolectric.buildActivity(ViewTrips.class, new Intent().putExtra("testing", true))) {
            controller.setup();
            ViewTrips activity = controller.get();

            assertEquals(activity.currUser.getName(), "Test");
        }
    }

    @Test
    public void testInsertTrips(){
        try (ActivityController<ViewTrips> controller = Robolectric.buildActivity(ViewTrips.class, new Intent().putExtra("testing", true))) {
            controller.setup();
            ViewTrips activity = controller.get();
            activity.insertTrips();

            assertEquals(activity.trip, "Test trip");
        }
    }


}
