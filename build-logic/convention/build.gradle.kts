import org.gradle.api.tasks.compile.JavaCompile
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    `kotlin-dsl`
}

val JVM_TARGET = project.extra["build.jvmtarget.host"] as String

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

dependencies {
    implementation(libs.android.gradle)
    implementation(libs.kotlin.gradle)
    implementation(libs.ksp.gradle)
    implementation(libs.gradle.develocity)
    implementation(libs.screenshot.testing.gradle)
    implementation(androidxLibs.room.gradle)
}

gradlePlugin {
    plugins {
        register("androidBuilder") {
            id = "com.myapplication.android.builder"
            implementationClass = "AndroidBuilderPlugin"
        }
        register("androidVersionCatalogsLoader") {
            id = "com.myapplication.android.versions.loader"
            implementationClass = "AndroidVersionCatalogsLoaderPlugin"
        }
        register("androidVersionCatalogsChecker") {
            id = "com.myapplication.android.versions.checker"
            implementationClass = "AndroidVersionCatalogsCheckerPlugin"
        }
    }
}
