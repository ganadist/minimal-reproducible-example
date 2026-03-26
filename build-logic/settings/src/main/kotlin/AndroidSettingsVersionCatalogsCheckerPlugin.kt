import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.extra
import org.gradle.util.internal.VersionNumber

@Suppress("unused")
class AndroidSettingsVersionCatalogsCheckerPlugin : Plugin<Settings> {
    override fun apply(target: Settings) {
        with(target) {
            @Suppress("UNCHECKED_CAST")
            val modules = gradle.extra[AndroidSettingsVersionCatalogsLoaderPlugin.MODULE_EXTRA]
                as Map<String, String>

            @Suppress("UnstableApiUsage")
            gradle.lifecycle.afterProject {
                configurations.all {
                    val isHostConfigurations =
                        name.endsWith("UnitTestCompileClasspath") ||
                                name.endsWith("UnitTestRuntimeClasspath") ||
                                name.endsWith("ScreenshotTestCompileClasspath") ||
                                name.endsWith("ScreenshotTestRuntimeClasspath")
                    val isIntermediatesConfiguration =
                        !isHostConfigurations &&
                                ( name.endsWith("RuntimeClasspath") ||
                                                name.endsWith("CompileClasspath") )

                    resolutionStrategy.eachDependency {
                        val module = "${requested.group}:${requested.name}"
                        val needToCheck = isIntermediatesConfiguration
                        if (needToCheck &&
                            module in modules &&
                            checkVersion(requested.version, modules[module])
                        ) {
                            throw GradleException(
                                "Requested library required higher version" +
                                        "($module:${requested.version})\n" +
                                        "than which is described at Version Catalogs" +
                                        "($module:${modules[module]}).\n" +
                                        "Please check your change is intended, " +
                                        "then update Version Catalogs.\n"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun checkVersion(version: String?, maxVersion: String?): Boolean {
        if (version == null) {
            return false
        }
        if (maxVersion == null || maxVersion == "unspecified") {
            return false
        }
        val versionNumber = VersionNumber.parse(version)
        val maxVersionNumber = VersionNumber.parse(maxVersion)
        return versionNumber > maxVersionNumber
    }
}
