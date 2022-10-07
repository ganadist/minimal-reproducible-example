package com.example.mylib

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.mockito.Mock
import org.mockito.Mockito.RETURNS_DEEP_STUBS
import org.mockito.kotlin.mock

@RunWith(RobolectricTestRunner::class)
class GlideRequestFactoryTest {
    private lateinit var context: Context

    @Mock
    private lateinit var mockRequestManager: GlideRequests

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        mockRequestManager = mock(defaultAnswer = RETURNS_DEEP_STUBS)
    }

    @Test
    fun testFactory() {
        val factory = GlideRequestFactory()
        val request = factory.createRequest(mockRequestManager)
        request.submit()
    }
}