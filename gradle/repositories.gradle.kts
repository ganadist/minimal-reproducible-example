import java.net.URI

/**
 This script provides maven repositories handler which are enabled content filtering.

 Please see details on
 https://docs.gradle.org/current/userguide/declaring_repositories.html#sec:repository-content-filtering
 */

abstract class RepositoryUrls {
    private val R8_SNAPSHOT_URL = "https://storage.googleapis.com/r8-releases/raw/master"

    // Base URL for repository
    // Also this URL is printed on build log to identify current repository
    abstract val baseUrl: String

    abstract val gradlePluginPortalUrl: String
    abstract val googleMavenUrl: String
    abstract val r8ReleaseUrl: String
    abstract val mavenCentralUrl: String

    private lateinit var logger: Logger
    private lateinit var settings: Settings

    protected lateinit var externalMavenCentralUrl: String

    fun setupSettings(settings: Settings, logger: Logger) {
        this.logger = logger
        this.settings = settings

        externalMavenCentralUrl = gradleProperty("build.maven.central.url")

        warn("Using maven repository from $baseUrl")

        settings.pluginManagement {
            repositories {
                maven {
                    url = URI("file://${settings.settingsDir}/.m2/local")
                }

                addR8()
                addGoogle()
                addGradlePluginPortal()
            }
        }

        settings.dependencyResolutionManagement {
            repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
            repositories {
                maven {
                    url = URI("file://${settings.settingsDir}/.m2/local")
                }

                addR8()
                addGoogle()

                addMavenCentral()
                if (settings.isBuildLogic()) {
                    addGradlePluginPortal()
                }
            }
        }

        // need to capture immutable url before perform beforeProject block
        val projectMavenCentralUrl = mavenCentralUrl
        settings.gradle.lifecycle.beforeProject {
            // This value is used to set robolectric.dependency.repo.url on each projects
            project.extra["MAVEN_CENTRAL_URL"] = projectMavenCentralUrl
        }
    }

    private fun Settings.isBuildLogic(): Boolean = (rootProject.name == "build-logic")

    private fun warn(msg: String) {
        if (!settings.isBuildLogic()) {
            logger.warn(msg)
        }
    }

    private fun gradleProperty(key: String, defaultValue: String = "") =
        settings.providers.gradleProperty(key).getOrElse(defaultValue)

    private fun RepositoryHandler.addR8() {
        val r8Version = gradleProperty("r8Version")
        if (r8Version.isEmpty()) {
            return
        }
        val r8RepoUrl = if (r8Version.contains(".")) r8ReleaseUrl else R8_SNAPSHOT_URL

        maven {
            url = URI(r8RepoUrl)
            content {
                includeModule("com.android.tools", "r8")
            }
        }
    }

    private fun RepositoryHandler.addGradlePluginPortal() {
        maven {
            url = URI(gradlePluginPortalUrl)
        }
    }

    private fun RepositoryHandler.addGoogle() {
        val androidXSnapshotBuildId: String = gradleProperty("androidXSnapshotBuildId")
        val studioSnapshotBuildId: String = gradleProperty("studioSnapshotBuildId")

        maven {
            url = URI(googleMavenUrl)
            content {
                ArtifactGroups.GOOGLE.forEach {
                    includeGroupAndSubgroups(it)
                }
                // exclude snapshot artifacts
                excludeVersionByRegex("androidx\\..*", ".*", ".*-SNAPSHOT")
                excludeVersionByRegex("androidx\\.databinding(\\..*|)", ".*", ".*-dev")
                excludeVersionByRegex("com\\.android(\\..*|)", ".*", ".*-dev.*")
            }
        }

        // https://androidx.dev/
        maven {
            url =
                URI(
                    "https://androidx.dev/snapshots/builds/$androidXSnapshotBuildId/artifacts/repository"
                )
            content {
                includeVersionByRegex("androidx\\..*", ".*", ".*-SNAPSHOT")
            }
        }

        maven {
            url =
                URI(
                    "https://androidx.dev/studio/builds/$studioSnapshotBuildId/artifacts/artifacts/repository"
                )
            content {
                includeVersionByRegex("androidx\\.databinding(\\..*|)", ".*", ".*-dev")
                includeVersionByRegex("com\\.android(\\..*|)", ".*", ".*-dev.*")
            }
        }
    }

    private fun RepositoryHandler.addMavenCentral() {
        maven {
            url = URI(mavenCentralUrl)
            content {
                val vendorArtifacts = ArtifactGroups.GOOGLE

                // Do not request vendor specific artifacts to maven-central repo
                vendorArtifacts.forEach {
                    excludeGroupAndSubgroups(it)
                }
            }
        }

        maven {
            url = URI(mavenCentralUrl)
            content {
                // legacy android annotations
                includeModule("com.google.android", "annotations")
            }
        }
    }
}

object ArtifactGroups {
    val GOOGLE = setOf(
        "androidx",
        "com.android",
        "com.crashlytics",
        "com.google.ads",
        "com.google.ai",
        "com.google.android",
        "com.google.androidbrowserhelper",
        "com.google.ar",
        "com.google.assistant",
        "com.google.firebase",
        "com.google.gms",
        "com.google.mlkit",
        "com.google.oboe",
        "com.google.play.policy",
        "com.google.prefab",
        "com.google.testing.platform",
        "org.chromium.net",
    )
}

object ExternalUrls : RepositoryUrls() {
    override val baseUrl: String
        get() = externalMavenCentralUrl

    override val gradlePluginPortalUrl: String
        get() = "https://plugins.gradle.org/m2"
    override val googleMavenUrl: String
        get() = "https://maven.google.com"
    override val r8ReleaseUrl: String
        get() = "https://storage.googleapis.com/r8-releases/raw"
    override val mavenCentralUrl: String
        get() = externalMavenCentralUrl
}


enum class Repos(val urls: RepositoryUrls) {
    NONE(ExternalUrls),
}

val repoProp = settings.providers.gradleProperty("build.repo").getOrElse("none")
val repo = Repos.entries.firstOrNull { it.name == repoProp.uppercase() } ?: throw GradleException(
    "Invalid value $repoProp for 'build.repo' gradle property. " +
        "Use one of: ${Repos.entries.joinToString { it.name.lowercase() }}"
)

repo.urls.setupSettings(settings, logger)
