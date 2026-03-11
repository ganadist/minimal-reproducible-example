package com.example.myapplication

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.config.Builder
import com.example.config.UserConfiguration
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.myapplication", appContext.packageName)
    }

    @Test
    fun testConfigBuilder() {
        Log.d("test", "test123")
        val user: UserConfiguration =
            Builder.from(
                mapOf(
                    "userId" to "[ { \"userId\": \"0000\" } ]",
                ),
            )
        assertEquals("0000", user.userId)
    }
}
