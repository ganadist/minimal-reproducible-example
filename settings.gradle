pluginManagement {
    apply from: "$settingsDir/gradle/build_requires_checker.gradle"
    apply from: "$settingsDir/gradle/repositories.gradle"

    buildscript {
	// r8Versoin is declared in gradle.properties
        if (!r8Version.isEmpty()) {
            settings.repos.r8(repositories, r8Version)

            dependencies {
                logger.warn("R8 $r8Version will be applied")
                classpath("com.android.tools:r8:$r8Version") {
                    exclude(group: "com.google.guava", module: "guava")
                }
            }
        }
    }

    settings.ext.repos.google(repositories)
    settings.ext.repos.gradlePluginPortal(repositories)

    plugins {
        id 'com.android.settings' version "$androidGradlePluginVersion"
        id 'com.gradle.enterprise' version "$gradleEnterprisePluginVersion"
    }
    includeBuild("build-logic")
}

plugins {
    id 'com.gradle.enterprise'
    id 'com.android.settings'
}

android {
    def _minSdk = settings.getProperty("minSdk").toInteger()
    def _compileSdkPreview = settings.getProperty("compileSdkPreview")
    def _compileSdkExtension = settings.getProperty("compileSdkExtension")
    def _buildToolsVersion = settings.getProperty("buildToolsVersion")

    compileSdk settings.getProperty("compileSdk").toInteger()

    if (!_compileSdkExtension.isEmpty()) {
        compileSdkExtension _compileSdkExtension.toInteger()
    }

    if (!_compileSdkPreview.isEmpty()) {
        compileSdkPreview _compileSdkPreview
    }

    minSdk _minSdk
    ndkVersion "$NDK_VERSION"
    buildToolsVersion _buildToolsVersion
    execution {
        defaultProfile "minimal"
        profiles {
            minimal {
                r8 {
                    runInSeparateProcess true
                    jvmOptions = ["-Xmx2g", "-XX:+UseParallelGC"]
                }
            }
        }
    }
}

dependencyResolutionManagement {
    settings.ext.repos.google(repositories)
    settings.ext.repos.mavenCentral(repositories)
}

apply from: "gradle/version_catalogs.gradle"

include ':app', ":shared"
include ":lib:hostconfig"
include ":tests:baselineprofile"
rootProject.name='My Application'
