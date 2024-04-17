@file:Suppress("UnstableApiUsage")

plugins {
    alias(libs.plugins.kmp)
    alias(libs.plugins.android.kmp.lib)
    alias(libs.plugins.android.lint)
}

// referenced from https://android.googlesource.com/platform/tools/base/+/81eb9107f6f/build-system/integration-test/test-projects/kotlinMultiplatform/kmpFirstLib/build.gradle.kts
kotlin {
    androidLibrary {
        namespace = "com.example.shared"
        withJava()
        withAndroidTestOnJvmBuilder {
            compilationName = "unitTest"
            defaultSourceSetName = "androidUnitTest"
        }.configure {
          isIncludeAndroidResources = true
        }

        withAndroidTestOnDeviceBuilder {
            compilationName = "instrumentedTest"
            defaultSourceSetName = "androidInstrumentedTest"
        }
    }
}

androidComponents {
    finalizeDsl { extension ->

    }

    onVariant { variant ->

    }
}
