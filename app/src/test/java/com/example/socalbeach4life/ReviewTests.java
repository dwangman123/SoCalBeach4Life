package com.example.socalbeach4life;
import static org.junit.Assert.assertEquals;
import static org.robolectric.annotation.LooperMode.Mode.PAUSED;

import android.content.Intent;
import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.LooperMode;

@RunWith(RobolectricTestRunner.class)
@LooperMode(PAUSED)
public class ReviewTests {
    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddToWrapper(){
        ReviewWrapper testWrapper = new ReviewWrapper("testBeach");
        Review testReview1 = new Review(5, "testDesc", "testBeach", false, "testUser");
        Review testReview2 = new Review(3, "testDesc", "testBeach", false, "testUser");

        testWrapper.addToReviews(testReview1);
        assertEquals(testWrapper.getRating(), 5, 0.0001);
        assertEquals(testWrapper.getReviewCount(), 1, 0.0001);

        testWrapper.addToReviews(testReview2);
        assertEquals(testWrapper.getRating(), 4, 0.0001);
        assertEquals(testWrapper.getReviewCount(), 2, 0.0001);
    }

    @Test
    public void testPostReview() {
        try (ActivityController<AddReviewActivity> controller = Robolectric.buildActivity(AddReviewActivity.class, new Intent().putExtra("testing", true))) {
            controller.setup();
            AddReviewActivity activity = controller.get();


            activity.postReview(new View(activity.getApplicationContext()));

            assertEquals(activity.testReview.getRating(), 5, .0001);
            assertEquals(activity.testReview.getDescription(), "testDescription");
            assertEquals(activity.testReview.getUserName(), "testUserName");
        }
    }

    @Test
    public void testDeleteReview() {
        try (ActivityController<ViewReviewsActivity> controller = Robolectric.buildActivity(ViewReviewsActivity.class, new Intent().putExtra("testing", true))) {
            controller.setup();
            ViewReviewsActivity activity = controller.get();

            ReviewWrapper wrapper = new ReviewWrapper("testBeach");
            Review testReview = new Review(5, "testDescription", "testId", false, "testUserName");
            wrapper.addToReviews(testReview);
            assertEquals(wrapper.getReviewCount(), 1);
            assertEquals(wrapper.getRating(), 5, .0001);

            activity.deleteReview(wrapper, testReview);
            assertEquals(activity.testReviewCount, 0);

        }
    }
}
