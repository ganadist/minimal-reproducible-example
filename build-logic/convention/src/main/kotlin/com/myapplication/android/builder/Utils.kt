package com.myapplication.android.builder

import com.android.build.api.dsl.CommonExtension
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.Date
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.kotlin.dsl.DependencyHandlerScope

typealias AGPCommonExtension = CommonExtension<*, *, *, *, *, *>

internal fun Project.getProperty(
    propertyName: String,
    defValue: String = ""
): String =
    if (rootProject.hasProperty(propertyName)) {
        rootProject.property(propertyName).toString()
    } else {
        defValue
    }

internal fun String?.toIntOrZero(): Int = (this ?: "").toIntOrNull() ?: 0

internal fun String.toCamelCase(): String = if (isEmpty()) {
    ""
} else {
    substring(0..0).uppercase() + substring(1)
}

internal fun getBuildDateTime(): Date {
    val timestamp = System.getenv("BUILD_TIMESTAMP") ?: return Date(0)
    val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z").parse(timestamp, ParsePosition(0))
    if (date == null) {
        System.err.println("Warn: Invalid BUILD_TIMESTAMP \"${timestamp}\": use current Date()")
        return Date()
    }

    return date
}

internal fun Project.execAndStdout(vararg args: String): String {
    val stdout = java.io.ByteArrayOutputStream()
    exec {
        workingDir = rootDir
        commandLine(*args)
        isIgnoreExitValue = true
        standardOutput = stdout
        errorOutput = java.io.OutputStream.nullOutputStream()
    }
    return stdout.toString().trim()
}

private fun DependencyHandlerScope.addLibrary(
    configuration: String,
    catalog: VersionCatalog,
    name: String,
    buildFlavor: String = "",
    buildType: String = ""
): Dependency? {
    var tmpConfiguration = ""
    for (c: String in listOf(buildFlavor, buildType, configuration)) {
        if (tmpConfiguration.isEmpty()) {
            tmpConfiguration = c
        } else {
            tmpConfiguration += c.toCamelCase()
        }
    }
    return add(tmpConfiguration, catalog.findLibrary(name).get())
}

internal fun DependencyHandlerScope.addBundle(
    configuration: String,
    catalog: VersionCatalog,
    name: String,
    buildFlavor: String = "",
    buildType: String = ""
): Dependency? {
    var tmpConfiguration = ""
    for (c in listOf(buildFlavor, buildType, configuration)) {
        if (tmpConfiguration.isEmpty()) {
            tmpConfiguration = c
        } else {
            tmpConfiguration += c.toCamelCase()
        }
    }
    return add(tmpConfiguration, catalog.findBundle(name).get())
}

internal fun DependencyHandlerScope.compileOnly(
    catalog: VersionCatalog,
    name: String,
    buildFlavor: String = "",
    buildType: String = ""
) = addLibrary("compileOnly", catalog, name, buildFlavor, buildType)

internal fun DependencyHandlerScope.runtimeOnly(
    catalog: VersionCatalog,
    name: String,
    buildFlavor: String = "",
    buildType: String = ""
) = addLibrary("runtimeOnly", catalog, name, buildFlavor, buildType)

internal fun DependencyHandlerScope.api(
    catalog: VersionCatalog,
    name: String,
    buildFlavor: String = "",
    buildType: String = ""
) = addLibrary("api", catalog, name, buildFlavor, buildType)

internal fun DependencyHandlerScope.implementation(
    catalog: VersionCatalog,
    name: String,
    buildFlavor: String = "",
    buildType: String = ""
) = addLibrary("implementation", catalog, name, buildFlavor, buildType)

internal fun DependencyHandlerScope.testImplementation(
    catalog: VersionCatalog,
    name: String,
    buildFlavor: String = "",
    buildType: String = ""
) = addLibrary("testImplementation", catalog, name, buildFlavor, buildType)

internal fun DependencyHandlerScope.androidTestImplementation(
    catalog: VersionCatalog,
    name: String,
    buildFlavor: String = "",
    buildType: String = ""
) = addLibrary("androidTestImplementation", catalog, name, buildFlavor, buildType)

internal fun DependencyHandlerScope.androidTestUtil(
    catalog: VersionCatalog,
    name: String,
    buildFlavor: String = "",
    buildType: String = ""
) = addLibrary("androidTestUtil", catalog, name, buildFlavor, buildType)

fun isAllowed(flavorName: String?, buildType: String?, isLibrary: Boolean) = when (flavorName) {
    "develop" -> buildType == "debug"
    "beta", "staging" -> {
        if (isLibrary) {
            true
        } else {
            buildType != "debug"
        }
    }
    "production", in Const.DOGFOOD_FLAVORS -> buildType == "release"
    else -> true
}
