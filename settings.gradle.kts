pluginManagement {
    apply(from = File(settingsDir, "gradle/build_requires_checker.gradle"))
    apply(from = File(settingsDir, "gradle/repositories.gradle"))

    val repos: java.util.Properties by settings.extra

    // r8Version is declared in gradle.properties
    val r8Version: String by settings
    val androidGradlePluginVersion: String by settings
    val gradleDevelocityPluginVersion: String by settings
    val gradleUserDataPluginVersion: String by settings
    val dokkaPluginVersion: String by settings
    val koverPluginVersion: String by settings
    val androidCacheFixPluginVersion: String by settings

    buildscript {
        if (!r8Version.isEmpty()) {
            repositories {
                (repos["r8"] as groovy.lang.Closure<*>).call(this, r8Version)
            }

            dependencies {
                logger.warn("R8 $r8Version will be applied")
                classpath("com.android.tools:r8:$r8Version") {
                    exclude(group = "com.google.guava", module = "guava")
                }
            }
        }
    }

    repositories {
        (repos["google"] as groovy.lang.Closure<*>).call(this)
        (repos["gradlePluginPortal"] as groovy.lang.Closure<*>).call(this)
    }

    plugins {
        id("com.android.settings") version androidGradlePluginVersion
        id("com.gradle.develocity") version gradleDevelocityPluginVersion
        id("com.gradle.common-custom-user-data-gradle-plugin") version gradleUserDataPluginVersion

        id("org.jetbrains.dokka") version dokkaPluginVersion
        id("org.gradle.android.cache-fix") version androidCacheFixPluginVersion
        id("org.jetbrains.kotlinx.kover.aggregation") version koverPluginVersion
    }
    includeBuild("build-logic")
}

plugins {
    id("com.gradle.develocity")
    id("com.gradle.common-custom-user-data-gradle-plugin")
    id("com.android.settings")
    id("org.jetbrains.kotlinx.kover.aggregation")

    id("com.myapplication.android.builder").apply(false)
}

develocity {
    buildScan {
        // public gradle scan server
        server.set("https://scans.gradle.com")
        termsOfUseUrl.set("https://gradle.com/help/legal-terms-of-use")
        termsOfUseAgree.set("yes")
        // must be false to submit scan on demand
        publishing.onlyIf { false }
    }
}

android {
    val minSdk: String by settings
    val compileSdk : String by settings
    val compileSdkPreview: String by settings
    val compileSdkExtension: String by settings
    val buildToolsVersion: String by settings

    this.minSdk = minSdk.toInt()
    this.compileSdk = compileSdk.toInt()
    this.buildToolsVersion = buildToolsVersion

    if (compileSdkExtension.isNotEmpty()) {
        this.compileSdkExtension = compileSdkExtension.toInt()
    }

    if (compileSdkPreview.isNotEmpty()) {
        this.compileSdkPreview = compileSdkPreview
    }

    """
    execution {
        defaultProfile = "minimal"
        profiles {
            create("minimal") {
                r8 {
                    // https://issuetracker.google.com/issues/283632726
                    runInSeparateProcess = false
                    jvmOptions.addAll(listOf("-Xmx2g", "-XX:+UseParallelGC"))
                }
            }
        }
    }
    """
}

dependencyResolutionManagement {
    val repos: java.util.Properties by settings.extra
    repositories {
        (repos["google"] as groovy.lang.Closure<*>).call(this)
        (repos["mavenCentral"] as groovy.lang.Closure<*>).call(this)
    }
}

apply(from = File(settingsDir, "gradle/version_catalogs.gradle"))

include(":app")
//include(":shared")
include(":lib:hostconfig")
include(":tests:baselineprofile")
rootProject.name = "My Application"

gradle.lifecycle.beforeProject {
    pluginManager.apply("com.myapplication.android.builder")
    // Need to migrate in future
    // pluginManager.apply("com.myapplication.android.versions.checker")
}
