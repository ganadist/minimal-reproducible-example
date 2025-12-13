package com.myproduct.myservice

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.verify

@RunWith(Parameterized::class)
class MyStatusObserverNotifyPlayStatusTest(private val testData: TestData) {
    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    //private lateinit var target: MusicPlayStatusObserver

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): List<Any> = listOf(
            TestData(
                myStatus = 1,
                expectedMyStatus = 1
            ),
            TestData(
                myStatus = 2,
                expectedMyStatus = 2
            ),
            TestData(
                myStatus = 3,
                expectedMyStatus = 3
            ),
            TestData(
                myStatus = 4,
                expectedMyStatus = 4
            ),
            TestData(
                myStatus = 5,
                expectedMyStatus = 5
            )
        )
    }

    @Before
    fun setUp() {
        // target = MyStatusObserver()
    }

    @Test
    fun notifyMyStatus() {
        // target.notifyMyStatus()
    }

    data class TestData(
        // parameters
        val myStatus: Int,

        // expected
        val expectedMyStatus: Int,
    )
}
