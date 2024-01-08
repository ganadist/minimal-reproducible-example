pluginManagement {
    repositories {
        google()
        maven {
            url = uri("https://maven-central-asia.storage-download.googleapis.com/maven2")
        }
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        maven {
            url = uri("https://maven-central-asia.storage-download.googleapis.com/maven2")
        }
    }
}

rootProject.name = "My Application"
include(":app")
 
