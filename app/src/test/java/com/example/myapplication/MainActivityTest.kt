package com.example.myapplication

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.myapplication.databinding.ActivityMainBinding
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.clearInvocations
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    private val binding: ActivityMainBinding = ActivityMainBinding.inflate(
        LayoutInflater.from(ApplicationProvider.getApplicationContext())
    )

    private val inflateView: () -> ActivityMainBinding = spy(value = { binding })

    @Before
    fun setUp() {
        MainActivityController(inflateView)
    }

    @Test
    fun test1() {
    }
}

class MainActivityController(
    private val inflateView: () -> ActivityMainBinding
) {
    private var binding: ActivityMainBinding? = null
    init {
        binding = inflateView()
    }
}
