package com.myapplication.android.builder

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.withType

private val JVM_VERSION = System.getProperty("java.specification.version")
private val JVM_OPTIONS = listOf(
    "-Xlint:all,-this-escape",
    //"-Werror",
)

internal fun Project.configureJava(javaVersion: JavaVersion) {
    tasks.withType<JavaCompile>().configureEach {
        options.compilerArgs.addAll(JVM_OPTIONS)
        sourceCompatibility = javaVersion.toString()
        targetCompatibility = javaVersion.toString()
    }
}
