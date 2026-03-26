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
}

gradlePlugin {
    plugins {
        register("androidSettingsVersionCatalogsLoader") {
            id = "com.myapplication.android.settings.versions.loader"
            implementationClass = "AndroidSettingsVersionCatalogsLoaderPlugin"
        }
        register("androidSettingsVersionCatalogsChecker") {
            id = "com.myapplication.android.settings.versions.checker"
            implementationClass = "AndroidSettingsVersionCatalogsCheckerPlugin"
        }
    }
}
