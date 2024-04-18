package com.myapplication.android.builder

import org.gradle.api.Project

@Suppress("UnstableApiUsage")
internal fun Project.configureLint() {
    android {
        lint {
            lintConfig = file("$rootDir/app/lint.xml")
            abortOnError = false
            checkReleaseBuilds = true
        }
    }
}
