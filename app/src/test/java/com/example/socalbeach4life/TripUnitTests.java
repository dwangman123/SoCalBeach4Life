package com.example.socalbeach4life;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.time.LocalDateTime;

@RunWith(RobolectricTestRunner.class)
public class TripUnitTests {

    @Test
    public void testUpload(){
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now();
        Trip t = new Trip(start, end, "Source", "End", new User());
        t.upload();
        assertEquals(t.tripData.get("source"), "Source");
    }
}
