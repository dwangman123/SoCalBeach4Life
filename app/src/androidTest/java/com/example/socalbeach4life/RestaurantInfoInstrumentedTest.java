package com.example.socalbeach4life;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RestaurantInfoInstrumentedTest {
    @Rule
    public ActivityScenarioRule<ViewRestaurant> activityRule =
            new ActivityScenarioRule<>(new Intent(ApplicationProvider.getApplicationContext(), ViewRestaurant.class).putExtra("testing", true));

    @Test
    public void testRestaurantDisplay() {
        onView(withText("Test restaurant")).check(matches(isDisplayed()));
    }



}
