dependencyResolutionManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

apply(from = "gradle/version_catalogs.gradle")

include(":convention")
