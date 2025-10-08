package com.myapplication.android.builder

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

@Suppress("UnstableApiUsage")
internal fun Project.configureAnnotationProcessors() {
    pluginManager.withPlugin(Const.KAPT_PLUGIN_ID) {
        throw GradleException(
            "Use ksp compiler instead of kapt on $project"
        )
    }
}
