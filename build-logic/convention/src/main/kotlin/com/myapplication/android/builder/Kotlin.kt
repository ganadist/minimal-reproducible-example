package com.myapplication.android.builder

import java.time.Duration
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureKotlin(javaVersion: JavaVersion) {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = javaVersion.toString()
            allWarningsAsErrors = true
            val compilerOptions = mutableListOf<String>()
            val enableK2 =
                rootProject.getProperty("kotlin.experimental.tryK2").toBoolean()
            if (enableK2) {
                // https://kotlinlang.org/docs/whatsnew-eap.html#how-to-enable-the-kotlin-k2-compiler
                // when enable k2, compiler always warns
                allWarningsAsErrors = false
            }

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