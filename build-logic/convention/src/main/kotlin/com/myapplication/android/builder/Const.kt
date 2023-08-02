package com.myapplication.android.builder

import org.gradle.api.JavaVersion
import org.gradle.util.internal.VersionNumber

object Const {
    val GOOGLE_PLAY_VERSIONS = mapOf(
        // to disable jetifier, play services 17.3 or higher requires
        "play-services-basement" to VersionNumber.parse("17.3.0"),
        "play-services-base" to VersionNumber.parse("17.3.0"),
    )

    const val KAPT_PLUGIN_ID = "org.jetbrains.kotlin.kapt"
    const val KSP_PLUGIN_ID = "com.google.devtools.ksp"
    const val KOTLIN_ANDROID_PLUGIN_ID = "org.jetbrains.kotlin.android"

    val BUILD_TYPES = listOf("debug", "releaseDebuggable", "release")
    val BUILD_FLAVORS = listOf(
        "develop",
        "beta",
        "staging",
        "production"
    )
    val DOGFOOD_FLAVORS = listOf(
        "dogfood",
        "dogfoodrc"
    )

    val JAVA_VERSION = JavaVersion.VERSION_11
}
