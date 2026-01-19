import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Text
import com.android.tools.screenshot.PreviewTest


class Test123 {
    @PreviewTest
    @Preview
    @Composable
    fun MyPreview() {
        Text("Hello World")
    }
}

