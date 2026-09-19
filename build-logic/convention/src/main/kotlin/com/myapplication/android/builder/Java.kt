package com.myapplication.android.builder

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.withType

private val JVM_VERSION = System.getProperty("java.specification.version")
private val JVM_OPTIONS =
    sequenceOf(
        "-Xlint:all",
        // Disable dangling-doc-comments which is enabled since Java 23
        // This project is mainly using Kotlin, and no one has interest about JavaDoc
        "-Xlint:-dangling-doc-comments",
        "-Werror",
    )

internal fun Project.configureJava(javaVersion: JavaVersion) {
    tasks.withType<JavaCompile>().configureEach {
        options.compilerArgs.addAll(JVM_OPTIONS)
        sourceCompatibility = javaVersion.toString()
        targetCompatibility = javaVersion.toString()
    }
}
