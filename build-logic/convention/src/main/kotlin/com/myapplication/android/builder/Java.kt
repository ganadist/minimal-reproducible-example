package com.myapplication.android.builder

import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile

private val JVM_OPTIONS = listOf("-Xlint:all", "-Werror")

internal fun Project.configureJava() {
    tasks.withType(JavaCompile::class.java).configureEach {
        options.compilerArgs.addAll(JVM_OPTIONS)
    }
}
