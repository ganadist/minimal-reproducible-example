package com.myapplication.android.builder

import org.gradle.api.Project

@Suppress("UnstableApiUsage")
internal fun Project.configureJacoco() {
    android {
        testCoverage {
            jacocoVersion = libs.findVersion("jacoco").get().requiredVersion
        }
    }
}
