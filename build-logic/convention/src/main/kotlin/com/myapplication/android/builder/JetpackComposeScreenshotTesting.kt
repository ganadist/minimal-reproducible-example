package com.myapplication.android.builder

import com.android.build.api.variant.HasHostTestsBuilder
import com.android.build.api.variant.HostTestBuilder
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureJetpackComposeScreenshotTesting() {
    if (!hasScreenshotTestSourceSet) {
        return
    }
    configureScreenshotTestingPlugin()
}

private fun Project.configureScreenshotTestingPlugin() {
    pluginManager.withPlugin(Const.COMPOSE_SCREENSHOT_PLUGIN_ID) {
        val variantFilterHostTest =
            getProperty(
                "build.variants.filter.hosttest",
            ).toBoolean()

        componentsExtension.apply {
            beforeVariants(selector().all()) { variant ->
                val allowHostTest =
                    variant.name in Const.ALLOWED_DEBUG_ONLY_VARIANTS ||
                        !variantFilterHostTest

                @Suppress("UnstableApiUsage")
                (variant as? HasHostTestsBuilder)?.hostTests?.apply {
                    get(HostTestBuilder.SCREENSHOT_TEST_TYPE)?.apply {
                        enable = false
                        if (allowHostTest) {
                            enable = true
                            enableCodeCoverage = true
                        }
                    }
                }
            }
        }

        // https://developer.android.com/studio/preview/compose-screenshot-testing?hl=en#setup
        android {
            @Suppress("UnstableApiUsage")
            experimentalProperties["android.experimental.enableScreenshotTest"] = true
        }

        dependencies {
            screenshotTestImplementation(libs, "screenshot-validation-api")
            screenshotTestImplementation(androidxLibs, "compose-ui-tooling-preview")
        }
    }
}
