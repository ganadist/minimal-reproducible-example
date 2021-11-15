package com.example.myapplication;

import android.app.Activity;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.widget.VideoView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class ExampleUnitTest1 {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private Activity mActivity;
    @Mock
    private Window mWindow;
    @Mock
    private ViewStub mViewStub;
    @Mock
    private View mViewContainer;
    @Mock
    private VideoView mVideoView;

    @Before
    public void setUp() {

    }

    @Test
    public void test1() {

    }
}