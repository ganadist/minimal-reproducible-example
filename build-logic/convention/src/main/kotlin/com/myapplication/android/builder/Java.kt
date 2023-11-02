package com.myapplication.android.builder

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile

private val JVM_VERSION = System.getProperty("java.specification.version")
private val JVM_OPTIONS = if (JVM_VERSION == "21") {
    // https://issuetracker.google.com/issues/294422895
    listOf("-Xlint:all,-this-escape", "-Werror")
} else {
    listOf("-Xlint:all", "-Werror")
}

internal fun Project.configureJava(javaVersion: JavaVersion) {
    tasks.withType(JavaCompile::class.java).configureEach {
        options.compilerArgs.addAll(JVM_OPTIONS)
        sourceCompatibility = javaVersion.toString()
        targetCompatibility = javaVersion.toString()
    }
}
