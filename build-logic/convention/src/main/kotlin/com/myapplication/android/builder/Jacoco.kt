package com.myapplication.android.builder

import org.gradle.api.Project

@Suppress("UnstableApiUsage")
internal fun Project.configureJacoco() {
    android {
        buildTypes {
            maybeCreate("debug").apply {
                enableUnitTestCoverage =
                    project.file("src/test/java").isDirectory ||
                    project.file("src/test/kotlin").isDirectory

                enableAndroidTestCoverage = false
            }
        }
        testCoverage {
            jacocoVersion = "0.8.8"
        }
    }
}
