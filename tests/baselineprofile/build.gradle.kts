plugins {
    alias(libs.plugins.android.test)
    alias(libs.plugins.kotlin.android)
    alias(androidxLibs.plugins.baselineprofile)
}

android {
    namespace = "com.example.baselineprofile"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["androidx.benchmark.suppressErrors"] = "EMULATOR"
    }

    targetProjectPath = ":app"

    testOptions.managedDevices {
        devices {
            create<com.android.build.api.dsl.ManagedVirtualDevice>("genBaselineProfile") {
                device = "Pixel 2"
                apiLevel = rootProject.properties["compileSdk"].toString().toInt()
                systemImageSource = "google"
            }
        }
    }

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

baselineProfile {
    managedDevices += "genBaselineProfile"
    useConnectedDevices = false
}

dependencies {
    // default dependencies are set by
    // build-logic/convention/src/main/kotlin/com/myapplication/android/builder/UnitTest.kt

    implementation(androidxLibs.benchmark.macro.junit4)
}
