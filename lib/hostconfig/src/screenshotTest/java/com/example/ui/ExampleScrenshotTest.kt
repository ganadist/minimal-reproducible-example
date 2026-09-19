package com.example.ui
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.android.tools.screenshot.PreviewTest

class ExampleScreenshotTest {
    @Composable
    @PreviewLightDark
    @PreviewTest
    fun testText() {
        Text("Hello World")
    }
}
