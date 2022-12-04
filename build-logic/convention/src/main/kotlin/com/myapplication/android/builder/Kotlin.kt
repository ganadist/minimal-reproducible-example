package com.myapplication.android.builder

import java.time.Duration
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureKotlin() {
    tasks.withType(KotlinCompile::class.java).configureEach {
        kotlinOptions {
            jvmTarget = Const.JAVA_VERSION.toString()
            allWarningsAsErrors = true
            val compilerOptions = mutableListOf<String>()

            // https://youtrack.jetbrains.com/issue/KT-52199
            // if (Const.kotlinParallelCompilationProjects.contains(project.path)) {
            if (false) {
                compilerOptions.add("-Xbackend-threads=8")
            }

            if (name.endsWith("UnitTestKotlin")) {
                compilerOptions.add("-opt-in=kotlin.RequiresOptIn")
            }
            freeCompilerArgs = freeCompilerArgs + compilerOptions
        }

        timeout.set(
            Duration.ofMinutes(
                rootProject.getProperty("build.timeout.kotlinCompile").toLong()
            )
        )
    }
}