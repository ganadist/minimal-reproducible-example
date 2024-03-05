@file:Suppress("UnstableApiUsage")

import androidx.baselineprofile.gradle.producer.BaselineProfileProducerExtension
import com.android.build.api.dsl.ManagedVirtualDevice

plugins {
    alias(libs.plugins.android.test)
    alias(libs.plugins.kotlin.android)
}

private fun Project.getProperty(name: String): String {
    return rootProject.properties[name].toString()
}

private fun String?.toBoolean(): Boolean {
    return when (this) {
        "1",
        "yes",
        "true" -> true
        else -> false
    }
}

val enableBaselineProfileGenerator = getProperty(
    "build.baselineprofile.generate.enable"
).toBoolean()

if (enableBaselineProfileGenerator) {
    apply(plugin = "androidx.baselineprofile")

    android.testOptions.managedDevices.devices {
        create<ManagedVirtualDevice>("genBaselineProfile") {
            device = "Pixel 2"
            apiLevel = getProperty("compileSdk").toInt()
            systemImageSource = "google"
        }
    }

    extensions.findByType(BaselineProfileProducerExtension::class.java)?.apply {
        managedDevices += "genBaselineProfile"
        useConnectedDevices = false
    }
}

android {
    namespace = "com.example.baselineprofile"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["androidx.benchmark.suppressErrors"] = "EMULATOR"
    }

    targetProjectPath = ":app"

    flavorDimensions.add("default")
    productFlavors {
        // DO NOT allow develop flavor for benchmarking.
        create("rc")
        create("production")
    }
    buildTypes {
        // DO NOT add debug build type for benchmarking.
        maybeCreate("release")
    }
}

dependencies {
    // default dependencies are set by
    // build-logic/convention/src/main/kotlin/com/myapplication/android/builder/UnitTest.kt

    implementation(androidxLibs.benchmark.macro.junit4)
}
