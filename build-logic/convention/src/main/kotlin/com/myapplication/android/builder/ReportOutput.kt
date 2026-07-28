package com.myapplication.android.builder

import org.gradle.api.Project

@Suppress("UnstableApiUsage")
internal fun Project.configureReportOutput() {
    val changeReport: Boolean = getProperty("build.changereportdir").toBoolean()
    val basename = path.substring(1).replace(":", "_")
    val buildDir = project.isolated.rootProject.projectDirectory.dir("build")
    val rootReportDir = buildDir.dir("reports")
    val rootResultDir = buildDir.dir("test-results")

    val lintReportDir = rootReportDir.dir("$basename/lint")
    val testReportDir = rootReportDir.dir("$basename/test")
    val testResultDir = rootResultDir.dir("$basename/test")


    if (changeReport) {
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
    }
}
