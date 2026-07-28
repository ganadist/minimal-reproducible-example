apply(from = File(settingsDir, "gradle/repositories.gradle.kts"))
apply(from = File(settingsDir, "gradle/version_catalogs.gradle"))

include(":convention")
include(":settings")
