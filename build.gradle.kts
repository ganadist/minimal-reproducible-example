// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        // https://issuetracker.google.com/issues/431147146
        // Set Kotlin and KSP version strictly to avoid version conflict with
        // Android Gradle Plugin 9.0+
        classpath(libs.kotlin.gradle) {
            version {
                strictly(libs.versions.kotlin.get())
            }
        }
        classpath(libs.ksp.gradle) {
            version {
                strictly(libs.versions.ksp.get())
            }
        }
    }
}

plugins {
    alias(libs.plugins.android.app).apply(false)
    alias(libs.plugins.android.lib).apply(false)
    alias(libs.plugins.android.kmp.lib).apply(false)
    alias(libs.plugins.android.lint).apply(false)
    alias(libs.plugins.android.dfm).apply(false)
    alias(libs.plugins.android.test).apply(false)
    alias(libs.plugins.screenshot.testing).apply(false)
    alias(androidxLibs.plugins.baselineprofile).apply(false)

    alias(libs.plugins.kotlin.android).apply(false)
    alias(libs.plugins.kotlin.compose).apply(false)
    alias(libs.plugins.kotlin.kapt).apply(false)
    alias(libs.plugins.kotlin.parcelize).apply(false)
    alias(libs.plugins.kotlin.serialization).apply(false)
    alias(libs.plugins.kmp).apply(false)
    alias(libs.plugins.ksp).apply(false)

    id("com.myapplication.android.builder").apply(false)
    id("com.myapplication.android.versions.loader")
    id("com.myapplication.android.versions.checker").apply(false)
    alias(libs.plugins.gradle.develocity).apply(false)
}

apply(from = "$rootDir/gradle/build_constant.gradle")
apply(from = "$rootDir/gradle/wrapper.gradle")

subprojects {
    project.apply(plugin = "com.myapplication.android.builder")
    project.apply(plugin = "com.myapplication.android.versions.checker")
}

tasks.register("printKotlinVersion") {
    doLast {
        println("Kotlin version: ${org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion(logger)}")
    }
}
