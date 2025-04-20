@file:Suppress("UnstableApiUsage")

plugins {
    alias(libs.plugins.kmp)
    alias(libs.plugins.android.kmp.lib)
    alias(libs.plugins.android.lint)
}

// referenced from https://android.googlesource.com/platform/tools/base/+/f794086/build-system/integration-test/test-projects/kotlinMultiplatform/kmpFirstLib/build.gradle.kts
kotlin {
    androidLibrary {
        withJava()
        withHostTestBuilder {}.configure { isIncludeAndroidResources = true }
        withDeviceTest {}

        compilations.withType(com.android.build.api.dsl.KotlinMultiplatformAndroidDeviceTestCompilation::class.java) {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    sourceSets.getByName("androidDeviceTest") {

    }

    // https://youtrack.jetbrains.com/issue/KT-61573/#focus=Comments-27-10358357.0-0
    targets.configureEach {
        compilations.configureEach {
            compileTaskProvider.get().compilerOptions {
                freeCompilerArgs.add("-Xexpect-actual-classes")
            }
        }
    }
}

androidComponents {
    finalizeDsl { extension ->
        extension.namespace = "com.example.shared"
    }

    onVariant { variant ->

    }
}
