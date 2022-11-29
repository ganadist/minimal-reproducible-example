package com.myapplication.android.builder

import com.android.build.api.dsl.CommonExtension
import java.time.Duration
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.retry

@Suppress("UnstableApiUsage")
internal fun Project.configureUnitTest(
    commonExtension: CommonExtension<*, *, *, *>
) {
    plugins.apply("org.gradle.test-retry")

    val hasDynamicFeatureModulePlugin = pluginManager.hasPlugin("com.android.dynamic-feature")
    commonExtension.apply {
        defaultConfig {
            // https://developer.android.com/training/testing/junit-runner#ato-gradle
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

            // The following argument makes the Android Test Orchestrator run its
            // "pm clear" command after each test invocation. This command ensures
            // that the app's state is completely cleared between tests.
            testInstrumentationRunnerArguments["clearPackageData"] = "true"
        }

        testOptions {
            execution = "ANDROIDX_TEST_ORCHESTRATOR"

            unitTests {
                // http://robolectric.org/getting-started/#building-with-android-studiogradle
                // AGP does not support includeAndroidResources option for robolectric unittest on dynamic features, yet.
                isIncludeAndroidResources = !hasDynamicFeatureModulePlugin
            }

            // https://docs.gradle.org/current/dsl/org.gradle.api.tasks.testing.Test.html#N281DB
            // These values can be adjusted on Jenkins job configuration
            val testMaxForks = getProperty("build.unittest.maxforks", "0").toIntOrZero()
            val testJvmArgs = getProperty("build.unittest.jvmargs")
            val testForkEvery = getProperty("build.unittest.forkevery", "8").toLong()
            val testHeapSizeMin = getProperty("build.unittest.heapsize.min", "1g")
            val testHeapSizeMax = getProperty("build.unittest.heapsize.max", "6g")
            val testEnableLogging = getProperty("build.unittest.logging", "true").toBoolean()

            unitTests.all {
                if (testMaxForks == 0) {
                    it.maxParallelForks =
                        (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)
                } else {
                    it.maxParallelForks = testMaxForks
                }
                it.minHeapSize = testHeapSizeMin
                it.maxHeapSize = testHeapSizeMax

                if (testJvmArgs.isNotBlank()) {
                    it.jvmArgs = testJvmArgs.split(" ")
                }
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

    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    val androidxLibs = extensions.getByType<VersionCatalogsExtension>().named("androidxLibs")
    val googleLibs = extensions.getByType<VersionCatalogsExtension>().named("googleLibs")

    dependencies {
        testImplementation(libs, "junit4")
        testImplementation(libs, "robolectric")

        testImplementation(libs, "mockito-core")
        // Add mockito-inline dependency to mocking kotlin class easily
        testImplementation(libs, "mockito-inline")

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

    tasks.withType(Test::class.java).configureEach {
        timeout.set(
            Duration.ofMinutes(
                rootProject.getProperty("build.timeout.unittest").toLong()
            )
        )
    }
}
