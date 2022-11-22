package com.example.socalbeach4life;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.google.android.gms.maps.GoogleMap;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MapInstrumentedTest {
    @Rule
    public ActivityScenarioRule<MapsActivity> activityRule =
            new ActivityScenarioRule<>(new Intent(ApplicationProvider.getApplicationContext(), MapsActivity.class).putExtra("testing", true));

    @Rule
    public IntentsTestRule<ViewTrips> intentsTestRule =
            new IntentsTestRule<>(ViewTrips.class);

    @Test
    public void testFooterAppears() {
        onView(withText("Trips")).check(matches(isDisplayed()));
    }

    @Test
    public void testMapAppears(){
        onView(withId(R.id.map)).check(matches(isDisplayed()));
    }

    @Test
    public void testMarkerAppears(){
        UiDevice uiDevice = UiDevice.getInstance(getInstrumentation());
        UiObject mMarker1 = uiDevice.findObject(new UiSelector().descriptionContains("Test"));
        assertNotNull(mMarker1);
    }

    @Test
    public void testMarkerOnClick(){
        UiDevice uiDevice = UiDevice.getInstance(getInstrumentation());
        UiObject mMarker1 = uiDevice.findObject(new UiSelector().descriptionContains("Test"));
        try {
            mMarker1.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        UiObject infoWindow = uiDevice.findObject(new UiSelector().descriptionContains("None"));
        assertEquals(infoWindow.exists(), true);
    }

    @Test
    public void testGoToTrips(){
        onView(withText("Trips")).perform(click());
        intended(hasComponent(ViewTrips.class.getName()));
    }

    @Test
    public void testGoToReviews(){
        onView(withText("Reviews")).perform(click());
        intended(hasComponent(ReviewMainActivity.class.getName()));
    }

    @Test
    public void testGoToUserPage(){
        onView(withText("User page")).perform(click());
        intended(hasComponent(LogoutActivity.class.getName()));
    }
}