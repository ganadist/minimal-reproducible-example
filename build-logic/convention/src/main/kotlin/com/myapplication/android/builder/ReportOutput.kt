package com.myapplication.android.builder

import org.gradle.api.Project

@Suppress("UnstableApiUsage")
internal fun Project.configureReportOutput() {
    val changeReport: Boolean = getProperty("build.changereportdir").toBoolean()
    val basename = path.substring(1).replace(":", "_")
    val buildDir =
        project.isolated.rootProject.projectDirectory
            .dir("build")
    val reportsDir = buildDir.dir("reports/$basename")
    val testResultsDir = buildDir.dir("test-results/$basename")

    if (changeReport) {
        android {
            lint {
                xmlOutput = reportsDir.file("lint-results.xml").asFile
                htmlOutput = reportsDir.file("lint-results.html").asFile
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
