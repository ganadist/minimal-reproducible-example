package com.myapplication.android.builder

import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

@Suppress("UnstableApiUsage")
internal fun Project.configureAnnotationProcessors(
    @Suppress("Unused_parameter") commonExtension: AGPCommonExtension
) {
    pluginManager.withPlugin(Const.KAPT_PLUGIN_ID) {
        throw GradleException(
            "Use ksp compiler instead of kapt on $project"
        )
    }

    pluginManager.withPlugin(Const.KSP_PLUGIN_ID) {
        extensions.configure<KspExtension> {
            arg("room.incremental", "true")
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }
}