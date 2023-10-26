import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.DynamicFeatureExtension
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BasePlugin
import com.android.build.gradle.DynamicFeaturePlugin
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.TestPlugin
import com.myapplication.android.builder.Const
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
import org.gradle.kotlin.dsl.get

@Suppress("UnstableApiUsage")
class AndroidBuilderPlugin : Plugin<Project> {
    override fun apply(
        target: Project
    ) {
        with(target) {
            plugins.withType(BasePlugin::class.java) {
                val bytecodeVersion = JavaVersion.toVersion(
                    getProperty("build.jvmtarget.intermediates")
                )

                extensions.getByType(CommonExtension::class.java).let { android ->
                    /*
                    extensions.getByType(AndroidComponentsExtension::class.java).apply {
                        beforeVariants(selector().all()) { variant ->
                            when(variant.flavorName) {
                                "" -> variant.enable = true
                                "develop" -> variant.enable = variant.buildType == "debug"
                                else -> variant.enable = variant.buildType == "release"
                            }
                        }
                    }
                    */

                    configureAndroid(android, bytecodeVersion)
                    configureAnnotationProcessors(android)
                    configureLint(android)
                    configureTest(android)
                    configureJacoco(android)
                    configureReportOutput(android)
                    configureJava(bytecodeVersion)
                    configureKotlin(bytecodeVersion)
                    configureJetpackCompose(android)
                }
            }

            plugins.withType(LibraryPlugin::class.java) {
                configureTestDependencies()
            }

            plugins.withType(AppPlugin::class.java) {
                configureApplication(extensions.getByType(ApplicationExtension::class.java))
                configureTestDependencies()
            }

            plugins.withType(DynamicFeaturePlugin::class.java) {
                configureDynamicFeature(extensions.getByType(DynamicFeatureExtension::class.java))
                configureTestDependencies()
            }

            plugins.withType(TestPlugin::class.java) {
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
}
