package com.myapplication.android.builder

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

@Suppress("UnstableApiUsage")
internal fun Project.configureLint(
    commonExtension: CommonExtension<*, *, *, *>
) {
    commonExtension.apply {
        lint {
            lintConfig = file("$rootDir/app/lint.xml")
            abortOnError = false
            checkReleaseBuilds = true
        }
    }
}
