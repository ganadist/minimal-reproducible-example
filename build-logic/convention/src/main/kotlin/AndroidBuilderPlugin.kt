import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BasePlugin
import com.android.build.gradle.DynamicFeaturePlugin
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.TestPlugin
import com.myapplication.android.builder.componentsExtension
import com.myapplication.android.builder.configureAndroid
import com.myapplication.android.builder.configureAnnotationProcessors
import com.myapplication.android.builder.configureApplication
import com.myapplication.android.builder.configureDynamicFeature
import com.myapplication.android.builder.configureJacoco
import com.myapplication.android.builder.configureJava
import com.myapplication.android.builder.configureJetpackCompose
import com.myapplication.android.builder.configureKotlin
import com.myapplication.android.builder.configureLint
import com.myapplication.android.builder.configureReportOutput
import com.myapplication.android.builder.configureTest
import com.myapplication.android.builder.configureTestDependencies
import com.myapplication.android.builder.configureTestProjectDependencies
import com.myapplication.android.builder.getProperty
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

@Suppress("UnstableApiUsage")
class AndroidBuilderPlugin : Plugin<Project> {
    override fun apply(
        target: Project
    ) {
        with(target) {
            plugins.withType<BasePlugin>().configureEach {
                val bytecodeVersion = JavaVersion.toVersion(
                    getProperty("build.jvmtarget.intermediates")
                )

                componentsExtension.apply {
                    beforeVariants(selector().all()) { variant ->
                        when(variant.flavorName) {
                            "" -> variant.enable = true
                            "develop" -> variant.enable = variant.buildType == "debug"
                            else -> variant.enable = variant.buildType in RELEASE_BUILD_TYPES
                        }
                    }
                }

                configureAndroid(bytecodeVersion)
                configureAnnotationProcessors()
                configureLint()
                configureTest()
                configureJacoco()
                configureReportOutput()
                configureJava(bytecodeVersion)
                configureKotlin(bytecodeVersion)
                configureJetpackCompose()
            }

            plugins.withType<LibraryPlugin>().configureEach {
                configureTestDependencies()
            }

            plugins.withType<AppPlugin>().configureEach {
                configureApplication()
                configureTestDependencies()
            }

            plugins.withType<DynamicFeaturePlugin>().configureEach {
                configureDynamicFeature()
                configureTestDependencies()
            }

            plugins.withType<TestPlugin>().configureEach {
                configureTestProjectDependencies()
            }

            plugins.withId("org.jetbrains.kotlin.jvm") {
                val bytecodeVersion = JavaVersion.toVersion(
                    getProperty("build.jvmtarget.host")
                )
                configureJava(bytecodeVersion)
                configureKotlin(bytecodeVersion)
            }
        }
    }

    companion object {
        private val RELEASE_BUILD_TYPES = arrayOf(
            "release",
            // https://issuetracker.google.com/issues/307478189
            // build types from baselineprofile plugin
            "benchmarkRelease",
            "nonMinifiedRelease"
        )
    }
}
