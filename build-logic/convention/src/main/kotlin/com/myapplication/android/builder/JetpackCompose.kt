package com.myapplication.android.builder

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

private const val CHECK_COMPOSE_VERSION_PROPERTY =
    "build.jetpack.compose.kotlinVersionCompatibilityCheck"
private const val SUPPRESS_KOTLIN_VERSION_OPTION =
    "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck="

@Suppress("UnstableApiUsage")
internal fun Project.configureJetpackCompose(
    commonExtension: CommonExtension<*, *, *, *>
) {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    val androidxLibs = extensions.getByType<VersionCatalogsExtension>().named("androidxLibs")
    val composeCompilerVersion = androidxLibs.findVersion("compose.compiler").get().toString()
    commonExtension.apply {
        composeOptions {
            kotlinCompilerExtensionVersion = composeCompilerVersion
        }
    }

    val hasLibraryPlugin = pluginManager.hasPlugin("com.android.library")
    val checkComposeVersion = getProperty(CHECK_COMPOSE_VERSION_PROPERTY).toBoolean()
    afterEvaluate {
        val composeFeature = commonExtension.buildFeatures.compose
        if (composeFeature == true) {
            if (false) {
                throw GradleException(
                    "Do not enable Jetpack Compose on $project, " +
                        "because kotlin compiler will be getting slower with " +
                        "too many source codes: " +
                        "https://issuetracker.google.com/issues/210920415"
                )
            }

            dependencies {
                val composeBom = platform(androidxLibs.findLibrary("compose-bom").get())
                
                // Expose compose bom for all dependencies configurations
                // Because all version catalog items about compose does not have version information
                arrayOf(
                    "api",
                    "compileOnly",
                    "implementation",
                    "runtimeOnly",
                    "androidTestImplementation"
                ).forEach {
                    add(it, composeBom)
                }

                addBundle("implementation", androidxLibs, "compose")

                // https://issuetracker.google.com/issues/209688774
                api(androidxLibs, "compose-runtime")

                // https://developer.android.com/jetpack/compose/tooling
                // https://issuetracker.google.com/issues/257312399
                if (hasLibraryPlugin) {
                    compileOnly(androidxLibs, "compose-ui-tooling-preview")
                } else {
                    implementation(androidxLibs, "compose-ui-tooling-preview")
                }
                runtimeOnly(androidxLibs, "compose-ui-tooling", buildType = "debug")

                // https://developer.android.com/jetpack/compose/testing#setup
                androidTestImplementation(androidxLibs, "compose-ui-test-junit4")
                runtimeOnly(androidxLibs, "compose-ui-test-manifest", buildType = "debug")
            }

            if (!checkComposeVersion) {
                tasks.withType(KotlinCompile::class.java).configureEach {
                    kotlinOptions {
                        freeCompilerArgs = freeCompilerArgs + listOf(
                            "-P",
                            SUPPRESS_KOTLIN_VERSION_OPTION +
                                libs.findVersion("kotlin").get().toString()
                        )
                    }
                }
            }
        }
    }
}
