import com.android.build.gradle.BasePlugin
import com.myapplication.android.builder.android
import com.myapplication.android.builder.androidxLibs
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

@Suppress("UnstableApiUsage")
class ComposeK2MigrationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins.withType<BasePlugin>().configureEach {
                val composeCompilerVersion =
                    androidxLibs.findVersion("compose.compiler").get().requiredVersion

                android {
                    buildFeatures.compose = true
                    composeOptions {
                        kotlinCompilerExtensionVersion = composeCompilerVersion
                    }
                }
            }
        }
    }
}