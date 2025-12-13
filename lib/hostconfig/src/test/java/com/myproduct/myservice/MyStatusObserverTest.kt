package com.myproduct.myservice

import kotlin.test.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.whenever

/**
 * Contains all unit test cases for [MyStatusObserver].
 */
class MyStatusObserverTest {
    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    //private lateinit var target: MyStatusObserver

    @Before
    fun setUp() {
        //target = MyStatusObserver()
    }

    @Test
    fun register() {
        //target.register(mockMusicPlayListener)
    }

    @Test
    fun unregister() {
        //target.unregister(mockMusicPlayListener)
    }
}


