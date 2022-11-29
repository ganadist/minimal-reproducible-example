package com.myapplication.android.builder

import com.android.build.api.dsl.CommonExtension
import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.getValue

@Suppress("UnstableApiUsage")
internal fun Project.configureAnnotationProcessors(
    commonExtension: CommonExtension<*, *, *, *>
) {
    pluginManager.withPlugin(Const.KAPT_PLUGIN_ID) {
        throw GradleException(
            "Use ksp compiler instead of kapt on $project"
                
        )
    }

    pluginManager.withPlugin(Const.KSP_PLUGIN_ID) {
        extensions.getByType(KspExtension::class.java).apply {
            arg("room.incremental", "true")
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }
}
