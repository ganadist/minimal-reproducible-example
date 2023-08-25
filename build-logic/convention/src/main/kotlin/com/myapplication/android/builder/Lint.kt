package com.myapplication.android.builder

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

@Suppress("UnstableApiUsage")
internal fun Project.configureLint(
    commonExtension: AGPCommonExtension
) {
    commonExtension.apply {
        lint {
            lintConfig = file("$rootDir/app/lint.xml")
            abortOnError = false
            checkReleaseBuilds = true
        }
    }
}
