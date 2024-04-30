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
                val composeCompilerVersion = androidxLibs.findVersion("compose.compiler").get().toString()
                android {
                    afterEvaluate {
                        if (buildFeatures.compose == true) {
                            throw GradleException(
                                "`android.buildFeatures.compose = true` will be deprecated. \n" +
                                    "Use compose gradle plugin like this.\n" +
                                    "\n" +
                                    "plugins { \n" +
                                    "    alias(libs.plugins.kotlin.compose)\n" +
                                    "}\n"
                            )
                        }
                        if (composeOptions.kotlinCompilerExtensionVersion != null) {
                            throw GradleException(
                                "android.composeOptions.kotlinCompilerExtensionVersion will be " +
                                    "deprecated.\n" +
                                    "Please remove its usage from your build.gradle file"
                            )
                        }
                        buildFeatures.compose = true
                        composeOptions {
                            kotlinCompilerExtensionVersion = composeCompilerVersion
                        }
                    }
                }
            }
        }
    }
}