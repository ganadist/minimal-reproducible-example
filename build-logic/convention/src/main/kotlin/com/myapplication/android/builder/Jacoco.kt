package com.myapplication.android.builder

import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke

@Suppress("UnstableApiUsage")
internal fun Project.configureJacoco() {
    android {
        testCoverage.jacocoVersion = libs.findVersion("jacoco").get().requiredVersion
    }
}
