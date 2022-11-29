import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.DynamicFeatureExtension
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BasePlugin
import com.android.build.gradle.DynamicFeaturePlugin
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
import com.myapplication.android.builder.configureUnitTest
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.get

@Suppress("UnstableApiUsage")
class AndroidBuilderPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            plugins.withType(BasePlugin::class.java) {
                val hasLibraryPlugin = pluginManager.hasPlugin("com.android.library")
                val hasTestPlugin = pluginManager.hasPlugin("com.android.test")

                extensions.getByType(CommonExtension::class.java).let { android ->
                    extensions.getByType(AndroidComponentsExtension::class.java).apply {
                        beforeVariants(selector().all()) { variant ->
                            when(variant.flavorName) {
                                "" -> variant.enable = true
                                "develop" -> variant.enable = variant.buildType == "debug"
                                else -> variant.enable = variant.buildType == "release"
                            }
                        }
                        onVariants(selector().all()) { variant ->
                            // https://kotlinlang.org/docs/ksp-quickstart.html#make-ide-aware-of-generated-code
                            // https://github.com/google/ksp/pull/1195
                            val sourceRoot = "$buildDir/generated/ksp/${variant.name}"
                            android.sourceSets[variant.name].kotlin.srcDir(sourceRoot)
                        }
                    }

                    configureAndroid(android)
                    configureAnnotationProcessors(android)
                    configureLint(android)
                    // testplugin does not provide configuration for unittest/androidTest
                    if (!hasTestPlugin) {
                        configureUnitTest(android)
                    }
                    configureJacoco(android)
                    configureReportOutput(android)
                    configureJava()
                    configureKotlin()
                    configureJetpackCompose(android)
                }
            }

            plugins.withType(AppPlugin::class.java) {
                configureApplication(extensions.getByType(ApplicationExtension::class.java))
            }

            plugins.withType(DynamicFeaturePlugin::class.java) {
                configureDynamicFeature(extensions.getByType(DynamicFeatureExtension::class.java))
            }
        }
    }
}
