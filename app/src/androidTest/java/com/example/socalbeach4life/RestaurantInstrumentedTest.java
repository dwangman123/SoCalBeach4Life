package com.example.socalbeach4life;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RestaurantInstrumentedTest {
    @Rule
    public ActivityScenarioRule<MapsActivity> activityRule =
            new ActivityScenarioRule<>(new Intent(ApplicationProvider.getApplicationContext(), MapsActivity.class).putExtra("testing", true));

    @Rule
    public IntentsTestRule<ViewTrips> intentsTestRule =
            new IntentsTestRule<>(ViewTrips.class);


    @Test
    public void testRadius1000Appears() {
        onView(withText("Radius: 1000")).check(matches(isDisplayed()));
    }

    @Test
    public void testRadius2000Appears() {
        onView(withText("Radius: 2000")).check(matches(isDisplayed()));
    }

    @Test
    public void testRadius3000Appears() {
        onView(withText("Radius: 3000")).check(matches(isDisplayed()));
    }

    // restaurants appear
    // restaurant markers appear
    @Test
    public void testRestaurantAppears(){
        UiDevice uiDevice = UiDevice.getInstance(getInstrumentation());
        UiObject mMarker1 = uiDevice.findObject(new UiSelector().descriptionContains("Test"));
        try {
            mMarker1.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        UiObject mMarker2 = uiDevice.findObject(new UiSelector().descriptionContains("Test2"));
        assertNotNull(mMarker2);
    }
    // restaurant window appers
    @Test
    public void testRestaurantOnClick(){
        UiDevice uiDevice = UiDevice.getInstance(getInstrumentation());
        UiObject mMarker1 = uiDevice.findObject(new UiSelector().descriptionContains("Test"));
        try {
            mMarker1.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        UiObject mMarker2 = uiDevice.findObject(new UiSelector().descriptionContains("Test2"));
        try {
            mMarker2.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }

        UiObject infoWindow = uiDevice.findObject(new UiSelector().descriptionContains("None"));
        assertEquals(infoWindow.exists(), true);
    }



}
