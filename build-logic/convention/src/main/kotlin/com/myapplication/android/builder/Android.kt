
package com.myapplication.android.builder

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.exclude
import org.gradle.kotlin.dsl.getByType
import org.gradle.util.internal.VersionNumber

private const val MIN_CHECKER_VERSION_STRING = "3.12.0"
private val MIN_CHECKER_VERSION = VersionNumber.parse(MIN_CHECKER_VERSION_STRING)

@Suppress("UnstableApiUsage")
internal fun Project.configureAndroid(
    commonExtension: CommonExtension<*, *, *, *, *>
) {
    commonExtension.apply {
        defaultConfig {
            compileOptions {
                sourceCompatibility = Const.JAVA_VERSION
                targetCompatibility = Const.JAVA_VERSION
            }
            vectorDrawables.useSupportLibrary = true
        }

        packagingOptions {
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

    configurations.all {
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
        }

        // Exclude libraries that provided by android platform
        exclude(group = "org.apache.httpcomponents")
        exclude(group = "org.json", module = "json")
        exclude(group = "xalan", module = "xalan")
    }
}
