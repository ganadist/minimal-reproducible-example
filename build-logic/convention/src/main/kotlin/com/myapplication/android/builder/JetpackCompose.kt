package com.myapplication.android.builder

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

@Suppress("UnstableApiUsage")
internal fun Project.configureJetpackCompose() {
    plugins.withId(Const.KOTLIN_COMPOSE_PLUGIN_ID) {
        applyComposeDependencies()
        configureJetpackComposeScreenshotTesting()
    }
}

private fun Project.applyComposeDependencies() {
    dependencies {
        val composeBom = platform(androidxLibs.findLibrary("compose-bom").get())

        // Expose compose bom for all dependencies configurations
        // Because all version catalog items about compose does not have version information
        setOf(
            "api",
            "compileOnly",
            "implementation",
            "runtimeOnly",
            "androidTestImplementation",
        ).forEach {
            add(it, composeBom)
        }

        addBundle("implementation", androidxLibs, "compose")

        // https://issuetracker.google.com/issues/209688774
        api(androidxLibs, "compose-runtime")

        // https://developer.android.com/jetpack/compose/tooling
        // https://issuetracker.google.com/issues/257312399
        if (hasLibPlugin) {
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
