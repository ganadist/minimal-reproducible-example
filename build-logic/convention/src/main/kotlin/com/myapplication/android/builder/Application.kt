package com.myapplication.android.builder

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal fun Project.configureApplication() {
    extensions.configure<ApplicationExtension> {
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
