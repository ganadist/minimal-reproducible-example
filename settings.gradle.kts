private fun Settings.getProperty(key: String, defvalue: String = ""): String =
    providers.gradleProperty(key).getOrElse(defvalue)

pluginManagement {
    apply(from = File(settingsDir, "gradle/build_requires_checker.gradle"))
    apply(from = File(settingsDir, "gradle/repositories.gradle.kts"))

    // Settings.getProperty of root block is not available on pluginManagement block
    fun Settings.getProperty(key: String, defvalue: String = ""): String =
        providers.gradleProperty(key).getOrElse(defvalue)

    // r8Version is declared in gradle.properties
    val r8Version = getProperty("r8Version")
    val androidGradlePluginVersion = getProperty("androidGradlePluginVersion")
    val gradleDevelocityPluginVersion = getProperty("gradleDevelocityPluginVersion")
    val gradleUserDataPluginVersion = getProperty("gradleUserDataPluginVersion")

    buildscript {
        if (!r8Version.isEmpty()) {
            dependencies {
                logger.warn("R8 $r8Version will be applied")
                classpath("com.android.tools:r8:$r8Version") {
                    exclude(group = "com.google.guava", module = "guava")
                }
            }
        }
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
    id("com.myapplication.android.settings.versions.loader")
    id("com.myapplication.android.settings.versions.checker")
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
    val minSdkVersion = getProperty("minSdk")
    val compileSdkVersion = getProperty("compileSdk")
    val compileSdkMinorVersion = getProperty("compileSdkMinor")
    val compileSdkPreviewVersion = getProperty("compileSdkPreview")
    val compileSdkExtensionVersion = getProperty("compileSdkExtension")
    val targetSdkVersion = getProperty("targetSdk")

    minSdk {
        version = release(minSdkVersion.toInt())
    }
    compileSdk {
        if (compileSdkPreviewVersion.isEmpty()) {
            version = release(compileSdkVersion.toInt()) {
                if (compileSdkMinorVersion.isNotEmpty()) {
                    minorApiLevel = compileSdkMinorVersion.toInt()
                }
                if (compileSdkExtensionVersion.isNotEmpty()) {
                    sdkExtension = compileSdkExtensionVersion.toInt()
                }
            }
        } else {
            version = preview(compileSdkPreviewVersion)
        }
    }
    targetSdk {
        version = release(targetSdkVersion.toInt())
    }

    buildToolsVersion = getProperty("buildToolsVersion")

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

apply(from = File(settingsDir, "gradle/version_catalogs.gradle"))

include(":app")
//include(":shared")
include(":lib:hostconfig")
include(":tests:baselineprofile")
rootProject.name = "My Application"
