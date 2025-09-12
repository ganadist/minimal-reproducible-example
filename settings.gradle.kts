pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
    }

    val androidGradlePluginVersion: String by settings

    plugins {
        id("com.android.settings") version androidGradlePluginVersion
    }
}

plugins {
    id("com.android.settings")
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
    repositories {
        google()
        mavenCentral()
    }

    val androidGradlePluginVersion: String by settings
    val kotlinVersion : String by settings
    val kspVersion : String by settings

    versionCatalogs {
        create("libs") {
            // load versions from gradle.properties
            version("android-gradle", androidGradlePluginVersion)
            version("kotlin", kotlinVersion)
            version("ksp", "${kotlinVersion}-${kspVersion}")
        }
    }
}

include(":app")
