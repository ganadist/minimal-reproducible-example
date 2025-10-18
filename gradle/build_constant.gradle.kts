import java.nio.file.Files
import java.util.Properties

private val BUILD_MAJOR: String = "MAJOR"
private val BUILD_MINOR = "MINOR"
private val BUILD_MICRO = "MICRO"

// https://developer.android.com/studio/publish/versioning.html#appversioning
private val VERSION_CODE_MAX = 2100000000
private val VERSION_CODE_BASE_MAJOR = 10000000
private val VERSION_CODE_BASE_MINOR = 100000
private val VERSION_CODE_BASE_MICRO = 10000
private val VERSION_CODE_BASE_BUILD_NUMBER = 1

private val versionProperties = Properties()
Files.newBufferedReader(file("${settingsDir}/version.properties").toPath()).use { br ->
    versionProperties.load(br)
}

fun checkMaxVersion(
    version: Int,
    maxVersion: Int,
    fieldName: String,
) {
    if (version >= maxVersion) {
        throw IllegalArgumentException("$fieldName must be lesser than $maxVersion")
    }
}

internal fun generateVersionCode(): Int {
    val major = versionProperties.getProperty(BUILD_MAJOR).toInt()
    val minor = versionProperties.getProperty(BUILD_MINOR).toInt()
    val micro = versionProperties.getProperty(BUILD_MICRO).toInt()
    val buildNumber = (System.getenv("BUILD_NUMBER") ?: "0").toInt()

    checkMaxVersion(major, VERSION_CODE_MAX / VERSION_CODE_BASE_MAJOR, "Major version")
    checkMaxVersion(minor, VERSION_CODE_BASE_MAJOR / VERSION_CODE_BASE_MINOR, "Minor version")
    checkMaxVersion(micro, VERSION_CODE_BASE_MINOR / VERSION_CODE_BASE_MICRO, "Micro version")
    checkMaxVersion(buildNumber, VERSION_CODE_BASE_MICRO / VERSION_CODE_BASE_BUILD_NUMBER, "Build Number")

    return major * VERSION_CODE_BASE_MAJOR +
        minor * VERSION_CODE_BASE_MINOR +
        micro * VERSION_CODE_BASE_MICRO +
        buildNumber * VERSION_CODE_BASE_BUILD_NUMBER
}

gradle.extra["versionCode"] = generateVersionCode()
gradle.extra["versionName"] =
    listOf(BUILD_MAJOR, BUILD_MINOR, BUILD_MICRO).let {
        it.map { key -> versionProperties.getProperty(key) }.joinToString(".")
    }
