package com.myapplication.android.builder

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

@Suppress("UnstableApiUsage")
internal fun Project.configureJetpackCompose() {
    plugins.withId(Const.KOTLIN_COMPOSE_PLUGIN_ID) {
        if (false) {
            throw GradleException(
                "Do not enable Jetpack Compose on $project, " +
                        "because kotlin compiler will be getting slower with " +
                        "too many source codes: " +
                        "https://issuetracker.google.com/issues/210920415"
            )
        }
        applyComposeDependencies()
    }

    plugins.withId(Const.KOTLIN_K2_MIGRATION_PLUGIN_ID) {
        applyComposeDependencies()
    }
}

private fun Project.applyComposeDependencies() {
    val hasLibraryPlugin = pluginManager.hasPlugin("com.android.library")
    dependencies {
        val composeBom = platform(androidxLibs.findLibrary("compose-bom").get())

        // Expose compose bom for all dependencies configurations
        // Because all version catalog items about compose does not have version information
        arrayOf(
            "api",
            "compileOnly",
            "implementation",
            "runtimeOnly",
            "androidTestImplementation"
        ).forEach {
            add(it, composeBom)
        }

        addBundle("implementation", androidxLibs, "compose")

        // https://issuetracker.google.com/issues/209688774
        api(androidxLibs, "compose-runtime")

        // https://developer.android.com/jetpack/compose/tooling
        // https://issuetracker.google.com/issues/257312399
        if (hasLibraryPlugin) {
            compileOnly(androidxLibs, "compose-ui-tooling-preview")
        } else {
            implementation(androidxLibs, "compose-ui-tooling-preview")
        }
        runtimeOnly(androidxLibs, "compose-ui-tooling", buildType = "debug")

        // https://developer.android.com/jetpack/compose/testing#setup
        androidTestImplementation(androidxLibs, "compose-ui-test-junit4")
        runtimeOnly(androidxLibs, "compose-ui-test-manifest", buildType = "debug")
    }
}