package com.myapplication.android.builder

import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension

@Suppress("UnstableApiUsage")
internal fun Project.configureJacoco() {
    android {
        testCoverage {
            jacocoVersion = libs.findVersion("jacoco").get().requiredVersion
        }
    }

    tasks.withType<Test>().configureEach {
        configure<JacocoTaskExtension> {
            // Required for JaCoCo + Robolectric
            // https://github.com/robolectric/robolectric/issues/2230
            isIncludeNoLocationClasses = true

            excludes = listOf(
                // Required for JDK 11 with the above
                // https://github.com/gradle/gradle/issues/5184#issuecomment-391982009
                "jdk.internal.*",
            )
        }
    }
}
