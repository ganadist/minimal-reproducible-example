package com.myapplication.android.builder

import com.android.build.api.dsl.DynamicFeatureExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

@Suppress("UnstableApiUsage")
internal fun Project.configureDynamicFeature(
    dynamicFeatureExtension: DynamicFeatureExtension
) {
    dynamicFeatureExtension.apply {
        buildTypes {
            maybeCreate("releaseDebuggable").apply {
                // initWith(BuildType.DEBUG)
                initWith(maybeCreate("debug"))
                matchingFallbacks.add("debug")
            }
        }

        flavorDimensions.add("default")
        productFlavors {
            maybeCreate("develop")
            maybeCreate("beta")
            maybeCreate("staging")
            maybeCreate("production")
            maybeCreate("dogfood").matchingFallbacks.add("beta")
            maybeCreate("dogfoodrc").matchingFallbacks.add("rc")
        }
    }

    dependencies {
        add("implementation", project(":app"))
    }
}
