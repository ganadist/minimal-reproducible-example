package com.example.myapplication;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.Robolectric;
import org.robolectric.annotation.LooperMode;

import static org.mockito.Mockito.spy;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@LooperMode(LooperMode.Mode.PAUSED)
public class UnitTest1 {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private MainActivity activity;
    private MainActivity mockActivity;

    @Before
    public void setup() {
        activity = Robolectric.buildActivity(MainActivity.class).setup().get();
        mockActivity = spy(activity);
    }

    @Test
    public void test1() {
        assertEquals("compare base context between activity and mockActivity",
                activity.getBaseContext(),
                mockActivity.getBaseContext());
    }
}
