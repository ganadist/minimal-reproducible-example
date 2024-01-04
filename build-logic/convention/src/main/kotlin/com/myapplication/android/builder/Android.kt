
package com.myapplication.android.builder

import org.gradle.api.GradleException
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.exclude
import org.gradle.kotlin.dsl.getByType
import org.gradle.util.internal.VersionNumber

private const val MIN_CHECKER_VERSION_STRING = "3.12.0"
private val MIN_CHECKER_VERSION = VersionNumber.parse(MIN_CHECKER_VERSION_STRING)

@Suppress("UnstableApiUsage")
internal fun Project.configureAndroid(
    commonExtension: AGPCommonExtension,
    javaVersion: JavaVersion
) {
    commonExtension.apply {
        defaultConfig {
            compileOptions {
                sourceCompatibility = javaVersion
                targetCompatibility = javaVersion
            }
            vectorDrawables.useSupportLibrary = true
        }

        packaging {
            jniLibs {
                pickFirsts.addAll(
                    arrayOf(
                        "**/libc++_shared.so",
                    )
                )
            }
            resources {
                excludes.addAll(
                    arrayOf(
                        "META-INF/LICENSE.md"
                    )
                )
            }
        }
    }

    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    val jacocoVersion = libs.findVersion("jacoco").get().requiredVersion

    val googleLibs = extensions.getByType<VersionCatalogsExtension>().named("googleLibs")
    val guavaAndroidVersion = googleLibs.findVersion("guava").get().requiredVersion

    configurations.all {
        resolutionStrategy.dependencySubstitution {
            // workaround for https://issuetracker.google.com/issues/316191239
            // inspired from https://github.com/androidx/androidx/blob/267fa9b/buildSrc/private/src/main/kotlin/androidx/build/AndroidXImplPlugin.kt#L755-L772
            // configuration name (${variant}RuntimeClasspath) is provided by Android Gradle Plugin
            if (name.endsWith("RuntimeClasspath")) {
                substitute(module("com.google.guava:listenablefuture"))
                    .using(module("com.google.guava:guava:$guavaAndroidVersion"))
            }
        }

        resolutionStrategy.eachDependency {
            if (requested.group.startsWith("com.android.support") ||
                requested.group.startsWith("android.arch")
            ) {
                throw GradleException(
                    "Do not add legacy $requested library on $project"
                )
            }

            if (requested.group == "androidx.databinding" &&
                requested.name == "databinding-compiler"
            ) {
                throw GradleException(
                    "Do not enable databinding compiler on $project"
                )
            }

            // version conflict between androidx.test and guava
            if (requested.group == "org.checkerframework" &&
                requested.name == "checker"
            ) {
                if (VersionNumber.parse(requested.version) < MIN_CHECKER_VERSION) {
                    useVersion(MIN_CHECKER_VERSION_STRING)
                }
            }

            if (requested.group == "com.google.android.gms" &&
                Const.GOOGLE_PLAY_VERSIONS.containsKey(requested.name)
            ) {
                val atLeastVersion: VersionNumber = Const.GOOGLE_PLAY_VERSIONS[requested.name]!!
                if (VersionNumber.parse(requested.version) < atLeastVersion) {
                    useVersion(atLeastVersion.toString())
                }
            }

            // workaround for https://issuetracker.google.com/issues/298703884
            if (requested.group == "org.jacoco" &&
                requested.name == "org.jacoco.agent" &&
                VersionNumber.parse(requested.version) < VersionNumber.parse(jacocoVersion)
            ) {
                useVersion(jacocoVersion)
            }
        }

        // Exclude libraries that provided by android platform
        exclude(group = "org.apache.httpcomponents")
        exclude(group = "org.json", module = "json")
        exclude(group = "xalan", module = "xalan")
    }
}
