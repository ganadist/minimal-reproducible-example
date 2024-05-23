package com.myapplication.android.builder

import java.time.Duration
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureKotlin(javaVersion: JavaVersion) {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(javaVersion.toString()))
            allWarningsAsErrors.set(true)
            val compilerOptions = mutableListOf<String>()

            // https://youtrack.jetbrains.com/issue/KT-52199
            // if (Const.kotlinParallelCompilationProjects.contains(project.path)) {
            if (false) {
                compilerOptions.add("-Xbackend-threads=8")
            }

            if (name.endsWith("UnitTestKotlin")) {
                optIn.add("kotlin.RequiresOptIn")
            }
            freeCompilerArgs.addAll(compilerOptions)
        }

        timeout.set(
            Duration.ofMinutes(
                rootProject.getProperty("build.timeout.kotlinCompile").toLong()
            )
        )
    }
}