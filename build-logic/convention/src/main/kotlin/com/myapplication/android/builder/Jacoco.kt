package com.myapplication.android.builder

import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.internal.coverage.JacocoReportTask
import org.gradle.api.Project
import org.gradle.api.file.ConfigurableFileTree

@Suppress("UnstableApiUsage")
internal fun Project.configureJacoco(
    commonExtension: CommonExtension<*, *, *, *>
) {
    commonExtension.apply {
        buildTypes {
            maybeCreate("debug").apply {
                enableUnitTestCoverage =
                    project.file("src/test/java").isDirectory ||
                    project.file("src/test/kotlin").isDirectory

                enableAndroidTestCoverage = false
            }
        }
        testCoverage {
            jacocoVersion = "0.8.8"
        }
    }

    afterEvaluate {
        tasks.withType(JacocoReportTask::class.java) {
            classFileCollection.forEach {
                println("cls: ${it.absolutePath}")
            }
            javaSources.get().forEach {
                println("src: ${it.asPath}")
            }
        }
    }
}
