import org.gradle.api.tasks.compile.JavaCompile
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    `kotlin-dsl`
}

val JVM_TARGET = rootProject.extra["build.jvmtarget.host"] as String

subprojects {
    tasks
        .withType<JavaCompile>()
        .configureEach {
            sourceCompatibility = JVM_TARGET
            targetCompatibility = JVM_TARGET
        }

    tasks
        .withType<KotlinJvmCompile>()
        .configureEach {
            compilerOptions
                .jvmTarget.set(JvmTarget.fromTarget(JVM_TARGET))
        }
}
