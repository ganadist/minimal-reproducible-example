import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest

// test code from https://issuetracker.google.com/issues/422488144#comment4
@Preview
@Composable
fun LazyColumnPreviewForScreenshotTest() {
    LazyColumn {
        item {
            Text("Text")
        }
    }
}

class AndroidAppScreenshotTest {
    @PreviewTest
    @Preview
    @Composable
    fun LazyColumnPreviewTest() {
        LazyColumnPreviewForScreenshotTest()
    }
}
