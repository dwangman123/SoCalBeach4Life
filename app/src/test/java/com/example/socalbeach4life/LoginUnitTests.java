package com.example.socalbeach4life;

import static org.robolectric.annotation.LooperMode.Mode.PAUSED;

import android.content.Context;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.robolectric.annotation.LooperMode;

@RunWith(MockitoJUnitRunner.class)
@LooperMode(PAUSED)
public class LoginUnitTests {
    @Mock
    Context mockContext;
}
