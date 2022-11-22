package com.example.socalbeach4life;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import static org.junit.Assert.assertEquals;

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
public class ReviewInstrumentedTest {
    @Rule
    public ActivityScenarioRule<ReviewMainActivity> activityRule =
            new ActivityScenarioRule<>(new Intent(ApplicationProvider.getApplicationContext(), ReviewMainActivity.class).putExtra("testing", true));

    @Rule
    public IntentsTestRule<ViewReviewsActivity> intentsTestRule =
            new IntentsTestRule<>(ViewReviewsActivity.class);

    @Test
    public void listOfBeachesAppear() {
        onView(withId(R.id.reviewsScrollView)).check(matches(isDisplayed()));
    }

    @Test
    public void beachOnclickWorks() throws UiObjectNotFoundException {
        onView(withText("Test")).perform(click());
        intended(hasComponent(ViewReviewsActivity.class.getName()));
    }

    @Test
    public void testBackToMap() {
        onView(withText("Return to map")).perform(click());
        intended(hasComponent(MapsActivity.class.getName()));
    }
}
