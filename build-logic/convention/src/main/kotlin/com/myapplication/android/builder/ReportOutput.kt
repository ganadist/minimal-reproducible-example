package com.myapplication.android.builder

import com.android.build.api.artifact.SingleArtifact
import com.android.build.gradle.internal.lint.AndroidLintTask
import com.android.compose.screenshot.tasks.PreviewScreenshotValidationTask
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType

/**
 * Collects this project's lint and test outputs under the root project's build directory when
 * `build.changereportdir` is enabled.
 *
 * Each project path maps to its own directory under `build/reports` and `build/test-results`. The
 * requested lint or test task still runs only for this Gradle project, but its reports and results
 * are copied or redirected to that directory tree so that CI can upload or copy outputs from
 * hundreds of subprojects in one operation.
 *
 * This differs from AGP report aggregation, which consumes results from all relevant test or lint
 * tasks in a module, including project dependencies for aggregated reports, and combines them into
 * one report. This function does not merge report contents or schedule tasks in other projects; it
 * only collects each project's existing outputs in a common directory tree.
 */
@Suppress("UnstableApiUsage")
internal fun Project.configureReportOutput() {
    val changeReport: Boolean = getProperty("build.changereportdir").toBoolean()
    val basename = path.substring(1).replace(":", "_")
    val buildDir =
        project.isolated.rootProject.projectDirectory
            .dir("build")
    val rootReportDir = buildDir.dir("reports")
    val rootResultDir = buildDir.dir("test-results")

    val lintReportDir = rootReportDir.dir("$basename/lint")
    val testReportDir = rootReportDir.dir("$basename/test")
    val androidTestReportDir = rootReportDir.dir("$basename/androidTest")
    val screenshotReportDir = rootReportDir.dir("$basename/screenshotTest")

    val testResultDir = rootResultDir.dir("$basename/test")
    val androidTestResultDir = rootResultDir.dir("$basename/androidTest")
    val screenshotResultDir = rootResultDir.dir("$basename/screenshotTest")

    val screenshotsBaseDir = buildDir.dir("screenshots/$basename")

    if (changeReport) {
        componentsExtension.apply {
            onVariants(selector().withBuildType("debug")) { variant ->
                // Since AGP 9.x, lint report relocation APIs were deprecated.
                // And need to use SigleArtifact APIs
                val lintXmlReportProvider = variant.artifacts.get(SingleArtifact.LINT_XML_REPORT)
                val lintHtmlReportProvider = variant.artifacts.get(SingleArtifact.LINT_HTML_REPORT)
                val copyLintXmlReportTask =
                    tasks.register<Copy>(
                        variant.computeTaskName("copy", "LintXmlReport"),
                    ) {
                        from(lintXmlReportProvider)
                        into(lintReportDir)
                        rename { "lint-results.xml" }
                    }
                val copyLintHtmlReportTask =
                    tasks.register<Copy>(
                        variant.computeTaskName("copy", "LintHtmlReport"),
                    ) {
                        from(lintHtmlReportProvider)
                        into(lintReportDir)
                        rename { "lint-results.html" }
                    }

                tasks.withType<AndroidLintTask>().configureEach {
                    if (name == "lintReport${variant.name.toCamelCase()}") {
                        finalizedBy(copyLintXmlReportTask)
                        finalizedBy(copyLintHtmlReportTask)
                    }
                }
            }
        }

        android {
            lint.apply {
                checkDependencies = false
            }
            testOptions.apply {
                unitTests.all {
                    it.reports {
                        html.outputLocation.set(testReportDir)
                        junitXml.outputLocation.set(testResultDir)
                    }
                }
            }
        }

        afterEvaluate {
            if (hasAndroidTestSourceSet) {
                // https://issuetracker.google.com/issues/219002669
                @Suppress("TYPEALIAS_EXPANSION_DEPRECATION")
                tasks.withType<DeprecatedManagedDeviceInstrumentationTestTask> {
                    getResultsDir().set(androidTestResultDir)
                    getReportsDir().set(androidTestReportDir)
                }
            }

            if (hasScreenshotTestSourceSet) {
                // need to migrate with configurationEach
                tasks.withType<PreviewScreenshotValidationTask> {
                    // name: validate${variant}ScreenshotTest
                    val variant = name.slice("validate", "ScreenshotTest")
                    // create same directory hierarchy for screenshotTest
                    val screenshotsDir =
                        screenshotsBaseDir.dir(
                            "src/screenshotTest$variant/reference",
                        )
                    testEngineInput.previewImageOutputDir.set(screenshotsDir)
                    reports {
                        html.outputLocation.set(screenshotReportDir)
                        junitXml.outputLocation.set(screenshotResultDir)
                    }
                }
            }
        }
    }
}
