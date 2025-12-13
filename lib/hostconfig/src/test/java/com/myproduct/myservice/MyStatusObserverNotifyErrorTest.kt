package com.myproduct.myservice

import kotlin.test.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(Parameterized::class)
class MyStatusObserverNotifyErrorTest(private val testData: TestData) {
    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    //private lateinit var target: MyStatusObserver

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): List<Any> = listOf(
            TestData(
                errorType = 1,
                expectedErrorType = 1
            ),
            TestData(
                errorType = 2,
                expectedErrorType = 2
            ),
            TestData(
                errorType = 3,
                expectedErrorType = 3
            ),
            TestData(
                errorType = 4,
                expectedErrorType = 4
            ),
            TestData(
                errorType = 5,
                expectedErrorType = 5
            ),
            TestData(
                errorType = 6,
                expectedErrorType = 6
            ),
            TestData(
                errorType = 7,
                expectedErrorType = 7
            ),
            TestData(
                errorType = 8,
                expectedErrorType = 8
            )
        )
    }

    @Before
    fun setUp() {
        //target = MyStatusObserver()
    }

    @Test
    fun notifyMyStatus() {
    }

    data class TestData(
        // parameters
        val myServiceId: String? = "serviceId",
        val errorMessage: String? = "errorMessage",
        val errorType: Int,

        // expected
        val expectedMusicId: String? = "serviceId",
        val expectedErrorType: Int
    )
}

