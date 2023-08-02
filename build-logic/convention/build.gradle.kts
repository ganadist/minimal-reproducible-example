import org.gradle.api.tasks.compile.JavaCompile
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    `kotlin-dsl`
}

tasks
    .withType<JavaCompile>()
    .configureEach {
        sourceCompatibility = JavaVersion.VERSION_17.toString()
        targetCompatibility = JavaVersion.VERSION_17.toString()
    }

tasks
    .withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>()
    .configureEach {
        compilerOptions
        .jvmTarget.set(JvmTarget.JVM_17)
    }

dependencies {
    implementation(libs.android.gradle)
    implementation(libs.kotlin.gradle)
    compileOnly(libs.ksp.gradle)
    compileOnly(libs.gradle.test.retry)
}

gradlePlugin {
    plugins {
        register("androidBuilder") {
            id = "com.myapplication.android.builder"
            implementationClass = "AndroidBuilderPlugin"
        }
    }
}
