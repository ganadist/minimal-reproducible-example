package com.myapplication.android.builder

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Project

internal fun Project.configureApplication(
    applicationExtension: ApplicationExtension
) {
    applicationExtension.apply {
        defaultConfig {
            targetSdk = getProperty("targetSdk").toIntOrZero()
        }

        productFlavors.all {
            if (name == "develop") {
                isDefault = true
            }
        }
    }
}
