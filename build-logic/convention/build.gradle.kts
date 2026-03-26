import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.CompileUsingKotlinDaemon
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilerExecutionStrategy

plugins {
    `kotlin-dsl`
}

private val jvmTarget = project.extra["build.jvmtarget.host"] as String

java {
    sourceCompatibility = JavaVersion.toVersion(jvmTarget)
    targetCompatibility = JavaVersion.toVersion(jvmTarget)
}

kotlin {
    compilerOptions.jvmTarget = JvmTarget.fromTarget(jvmTarget)
}

// To prevent memory consume from Kotlin Compiler daemon,
// Adjust Execution strategy to "in-process" for build-logic module
// Because Kotlin compiler version is different between build-logic and root project.
tasks
    .withType<CompileUsingKotlinDaemon>()
    .configureEach {
        compilerExecutionStrategy = KotlinCompilerExecutionStrategy.IN_PROCESS
    }

dependencies {
    implementation(libs.android.gradle)
    implementation(libs.kotlin.gradle)
    compileOnly(libs.ksp.gradle)
    compileOnly(libs.gradle.develocity)
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
