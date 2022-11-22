package com.example.socalbeach4life;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.robolectric.annotation.LooperMode.Mode.PAUSED;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.LooperMode;

@RunWith(RobolectricTestRunner.class)
@LooperMode(PAUSED)
public class LoginUnitTests {
    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateUser() {
            Authorization testAuth = new Authorization(true);

            testAuth.createUser("testName", "testEmail@email.com", "testPhone", "testPass");
            assertEquals(testAuth.testUser.get("password"), "testPass");
            assertEquals(testAuth.testUserObj.getName(), "testName");
            assertEquals(testAuth.testUserObj.getId(), "testId");
    }

    @Test
    public void testEmailValidator() {
        assertTrue(StartActivity.isEmailValid("testemail@gmail.com"));
        assertFalse(StartActivity.isEmailValid("testemail@gmail"));
        assertFalse(StartActivity.isEmailValid("testemail@gmailcom"));
        assertFalse(StartActivity.isEmailValid("testemail"));
    }
}
