pluginManagement {
    apply(from = File(settingsDir, "gradle/build_requires_checker.gradle"))
    apply(from = File(settingsDir, "gradle/repositories.gradle"))

    val repos: java.util.Properties by settings.extra

    // r8Version is declared in gradle.properties
    val r8Version: String by settings
    val androidGradlePluginVersion: String by settings
    val gradleDevelocityPluginVersion: String by settings
    val gradleUserDataPluginVersion: String by settings

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
    }
    includeBuild("build-logic")
}

plugins {
    id("com.gradle.develocity")
    id("com.gradle.common-custom-user-data-gradle-plugin")
    id("com.android.settings")
    id("com.myapplication.android.builder")
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
    minSdk {
        val minSdk: String by settings
        version = release(minSdk.toInt())
    }
    compileSdk {
        val compileSdk : String by settings
        val compileSdkMinor : String by settings
        val compileSdkPreview: String by settings
        val compileSdkExtension: String by settings

        if (compileSdkPreview.isEmpty()) {
            version = release(compileSdk.toInt()) {
                if (compileSdkMinor.isNotEmpty()) {
                    minorApiLevel = compileSdkMinor.toInt()
                }
                if (compileSdkExtension.isNotEmpty()) {
                    sdkExtension = compileSdkExtension.toInt()
                }
            }
        } else {
            version = preview(compileSdkPreview)
        }
    }
    targetSdk {
        val targetSdk : String by settings
        version = release(targetSdk.toInt())
    }

    val buildToolsVersion: String by settings
    this.buildToolsVersion = buildToolsVersion

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
apply(from = File(settingsDir, "gradle/build_constant.gradle.kts"))

include(":app")
include(":shared")
include(":lib:hostconfig")
include(":tests:baselineprofile")
rootProject.name = "My Application"
