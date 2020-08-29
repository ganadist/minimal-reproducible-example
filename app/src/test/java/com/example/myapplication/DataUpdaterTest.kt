package com.example.myapplication

import com.nhaarman.mockitokotlin2.any
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doAnswer
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DataUpdaterTest {
    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var dataManager: DataManager
    @Mock
    private lateinit var dataUpdater: DataUpdater

    @Before
    fun setup() {
        doAnswer {
            ((it.arguments[0]) as Function1<*, *>)(dataUpdater)
        }.`when`(dataManager).edit(any())
    }
}