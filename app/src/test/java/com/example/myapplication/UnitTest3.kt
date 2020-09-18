package com.example.myapplication

import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.runner.RunWith

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.*

import org.robolectric.annotation.LooperMode

@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.PAUSED)
class UnitTest3 {

    @Before
    fun setUp() {
        mockkObject(SomeExtensionAccess)
        every {
            SomeExtensionAccess.getExtensionModule()
                .getTimeStamp()
        } returns 1L
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun test1() {

    }
    @Test
    fun test2() {

    }

    @Test
    fun test3() {

    }
}