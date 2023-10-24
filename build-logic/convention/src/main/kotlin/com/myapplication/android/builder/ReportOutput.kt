package com.myapplication.android.builder

import org.gradle.api.Project

@Suppress("UnstableApiUsage")
internal fun Project.configureReportOutput(
    commonExtension: AGPCommonExtension
) {
    val changeReport: Boolean = getProperty("build.changereportdir").toBoolean()
    val basename = path.substring(1).replace(":", "_")
    val buildDir = rootProject.layout.buildDirectory.asFile.get()
    val reportsDir = file("${buildDir}/reports/$basename")
    val testResultsDir = file("${buildDir}/test-results/$basename")

    if (changeReport) {
        commonExtension.apply {
            lint {
                xmlOutput = file("$reportsDir/lint-results.xml")
                htmlOutput = file("$reportsDir/lint-results.html")
                checkDependencies = false
            }
            testOptions {
                unitTests.all {
                    it.reports {
                        html.outputLocation.set(reportsDir)
                        junitXml.outputLocation.set(testResultsDir)
                    }
                }
            }
        }
    }
}
