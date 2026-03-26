apply(from = File(settingsDir, "gradle/repositories.gradle"))

dependencyResolutionManagement {
    val repos: java.util.Properties by settings.extra
    repositories {
        (repos["google"] as groovy.lang.Closure<*>).call(this)
        (repos["gradlePluginPortal"] as groovy.lang.Closure<*>).call(this)
        (repos["mavenCentral"] as groovy.lang.Closure<*>).call(this)
    }
}

apply(from = File(settingsDir, "gradle/version_catalogs.gradle"))

include(":convention")
