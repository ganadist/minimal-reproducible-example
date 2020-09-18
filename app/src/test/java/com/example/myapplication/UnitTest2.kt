package com.example.myapplication

import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.runner.RunWith

import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.*
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

import org.robolectric.annotation.LooperMode

@RunWith(AndroidJUnit4::class)
@LooperMode(LooperMode.Mode.PAUSED)
class UnitTest2 {
    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    private val module = Mockito.mock(SomeExtensionModule::class.java)

    @Before
    fun setUp() {
        mockkObject(SomeExtensionAccess)
        every {
            SomeExtensionAccess.getExtensionModule()
        } returns module
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