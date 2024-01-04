package com.myapplication.android.builder

import com.android.build.api.dsl.ManagedVirtualDevice
import com.gradle.enterprise.gradleplugin.testretry.retry
import java.time.Duration
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

@Suppress("UnstableApiUsage")
internal fun Project.configureTest(
    commonExtension: AGPCommonExtension
) {
    val hasDynamicFeatureModulePlugin = pluginManager.hasPlugin("com.android.dynamic-feature")
    val hasTestModulePlugin = pluginManager.hasPlugin("com.android.test")
    val hasBaselineProfilePlugin = pluginManager.hasPlugin("androidx.baselineprofile")
    val androidTestApiLevel = getProperty("build.androidtest.sdk").toIntOrZero()
    commonExtension.apply {
        // https://developer.android.com/training/testing/instrumented-tests/androidx-test-libraries/runner?hl=en#enable-android
        defaultConfig {
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

            // The following argument makes the Android Test Orchestrator run its
            // "pm clear" command after each test invocation. This command ensures
            // that the app's state is completely cleared between tests.
            testInstrumentationRunnerArguments["clearPackageData"] = "true"
        }

        testOptions {
            // https://issuetracker.google.com/issues/314821647
            if (!hasTestModulePlugin && !hasBaselineProfilePlugin) {
                execution = "ANDROIDX_TEST_ORCHESTRATOR"
            }

            unitTests {
                // http://robolectric.org/getting-started/#building-with-android-studiogradle
                // AGP does not support includeAndroidResources option for robolectric unittest on dynamic features, yet.
                isIncludeAndroidResources = !hasDynamicFeatureModulePlugin
            }

            // https://developer.android.com/studio/test/gradle-managed-devices
            managedDevices {
                devices.register("gmd", ManagedVirtualDevice::class.java) {
                    device = "Pixel 2"

                    apiLevel = androidTestApiLevel
                    systemImageSource = "google-atd"
                }
            }


            // https://docs.gradle.org/current/dsl/org.gradle.api.tasks.testing.Test.html#N281DB
            // These values can be adjusted on Jenkins job configuration
            val testMaxForks = getProperty("build.unittest.maxforks", "0").toIntOrZero()
            val testForkEvery = getProperty("build.unittest.forkevery", "8").toLong()
            val testHeapSizeMin = getProperty("build.unittest.heapsize.min", "1g")
            val testHeapSizeMax = getProperty("build.unittest.heapsize.max", "6g")
            val testEnableLogging = getProperty("build.unittest.logging", "true").toBoolean()
            val testJvmArgs = mutableListOf(
                // https://developer.android.com/build/optimize-your-build#experiment-with-the-jvm-parallel-garbage-collector
                "-XX:+UseParallelGC",
                // https://github.com/robolectric/robolectric/issues/7456
                "--add-opens=java.base/java.lang=ALL-UNNAMED",
                "--add-opens=java.base/java.util=ALL-UNNAMED",
                // https://github.com/raphw/byte-buddy/issues/612#issuecomment-463618016
                "-Djdk.attach.allowAttachSelf=true",
                // https://github.com/mockito/mockito/issues/3037#issuecomment-1588199599
                "-XX:+EnableDynamicAgentLoading",
            ).apply {
                val jvmArgsFromProperty = getProperty("build.unittest.jvmargs")
                if (jvmArgsFromProperty.isNotBlank()) {
                    addAll(jvmArgsFromProperty.split(" "))
                }
            }


            unitTests.all {
                if (testMaxForks == 0) {
                    it.maxParallelForks =
                        (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)
                } else {
                    it.maxParallelForks = testMaxForks
                }
                it.minHeapSize = testHeapSizeMin
                it.maxHeapSize = testHeapSizeMax
                it.jvmArgs = testJvmArgs

                it.setForkEvery(testForkEvery)

                if (testEnableLogging) {
                    it.testLogging {
                        events("standardOut", "started", "passed", "failed", "skipped")
                        showCauses = true
                        showExceptions = true
                        showStackTraces = true
                    }
                    it.systemProperty("robolectric.logging", "stdout")
                }

                val roboDependencyUrlProp = "robolectric.dependency.repo.url"
                it.systemProperty(
                    roboDependencyUrlProp,
                    "https://maven-central-asia.storage-download.googleapis.com/maven2"
                )

                it.retry {
                    maxRetries.set(
                        getProperty("build.unittest.retry.max", "0").toIntOrZero()
                    )
                    maxFailures.set(
                        getProperty("build.unittest.retry.maxfailures", "0").toIntOrZero()
                    )
                    failOnPassedAfterRetry.set(
                        getProperty("bulid.unittest.retry.treatasfail", "false").toBoolean()
                    )
                }
            }
        }
    }

    tasks.withType(Test::class.java).configureEach {
        timeout.set(
            Duration.ofMinutes(
                rootProject.getProperty("build.timeout.unittest").toLong()
            )
        )
    }
}

@Suppress("UnstableApiUsage")
internal fun Project.configureTestDependencies() {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    val androidxLibs = extensions.getByType<VersionCatalogsExtension>().named("androidxLibs")
    val googleLibs = extensions.getByType<VersionCatalogsExtension>().named("googleLibs")

    dependencies {
        testImplementation(libs, "junit4")
        testImplementation(libs, "robolectric")

        testImplementation(libs, "mockito-core")

        pluginManager.withPlugin(Const.KOTLIN_ANDROID_PLUGIN_ID) {
            testImplementation(libs, "kotlin-test-junit")
            testImplementation(libs, "kotlinx-coroutines-test")
            testImplementation(libs, "mockito-kotlin")
        }

        addBundle("testImplementation", androidxLibs, "test")

        testImplementation(googleLibs, "truth")

        addBundle("androidTestImplementation", androidxLibs, "test")

        androidTestUtil(androidxLibs, "test-orchestrator")
        addBundle("androidTestImplementation", androidxLibs, "espresso")
    }
}

@Suppress("UnstableApiUsage")
internal fun Project.configureTestProjectDependencies() {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    val androidxLibs = extensions.getByType<VersionCatalogsExtension>().named("androidxLibs")
    dependencies {
        implementation(libs, "junit4")

        addBundle("implementation", androidxLibs, "test")
        addBundle("implementation", androidxLibs, "espresso")
        androidTestUtil(androidxLibs, "test-orchestrator")
    }
}
