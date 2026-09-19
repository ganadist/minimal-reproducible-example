package com.myapplication.android.builder

import org.gradle.api.JavaVersion
import org.gradle.util.internal.VersionNumber

object Const {
    val GOOGLE_PLAY_VERSIONS =
        mapOf(
            // to disable jetifier, play services 17.3 or higher requires
            "play-services-basement" to VersionNumber.parse("17.3.0"),
            "play-services-base" to VersionNumber.parse("17.3.0"),
        )

    val NATIVE_PAGE_SIZE_NOT_16KB_OR_HIGHER: Set<String> = setOf()

    const val ANDROID_APP_PLUGIN_ID = "com.android.application"
    const val ANDROID_LIB_PLUGIN_ID = "com.android.library"
    const val ANDROID_TEST_PLUGIN_ID = "com.android.test"
    const val COMPOSE_SCREENSHOT_PLUGIN_ID = "com.android.compose.screenshot"
    const val KAPT_PLUGIN_ID = "org.jetbrains.kotlin.kapt"
    const val KSP_PLUGIN_ID = "com.google.devtools.ksp"
    const val KOTLIN_ANDROID_PLUGIN_ID = "org.jetbrains.kotlin.android"
    const val KOTLIN_COMPOSE_PLUGIN_ID = "org.jetbrains.kotlin.plugin.compose"

    val BUILD_TYPES = setOf("debug", "releaseDebuggable", "release")
    val BUILD_FLAVORS =
        setOf(
            "develop",
            "beta",
            "staging",
            "production",
        )
    val DOGFOOD_FLAVORS =
        setOf(
            "dogfood",
            "dogfoodrc",
        )

    val ALLOWED_DEBUG_ONLY_VARIANTS =
        setOf(
            "debug",
            "developDebug",
        )

    val SRC_DIRS = setOf("java", "kotlin")
}
