package com.example.myapplication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import org.junit.Assert.*;
import com.myapplication.buildconfig.BuildConfig;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(RobolectricTestRunner.class)
public class TestPlue {
    @Test
    public void addition_isCorrect() {
        System.out.println(BuildConfig.Pluu);
    }
}
