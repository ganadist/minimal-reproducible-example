import androidx.baselineprofile.gradle.consumer.BaselineProfileConsumerExtension

plugins {
    id("com.android.application")
    alias(libs.plugins.kotlin.compose)
}

val enableL8 = providers.gradleProperty("build.l8.enable")
    .getOrElse("false").toBoolean()
val enableBaselineProfileGenerator =
    providers.gradleProperty("build.baselineprofile.generate.enable")
        .getOrElse("false").toBoolean()
var enableDexlayoutOptimizer =
    providers.gradleProperty("build.dexlayout.optimize.enable")
        .getOrElse("false").toBoolean()
if (!enableBaselineProfileGenerator && enableDexlayoutOptimizer) {
    logger.warn("Baseline Profile generator was disabled, and cannot enable Dex Layout Optimizer")
    enableDexlayoutOptimizer = false
}

if (enableBaselineProfileGenerator) {
    apply(plugin = "androidx.baselineprofile")
}

android {
    namespace = "com.example.myapplication"
    defaultConfig {
        applicationId = "com.example.myapplication"
        versionCode = rootProject.extra["versionCode"] as Int
        versionName = rootProject.extra["versionName"] as String
        compileOptions.isCoreLibraryDesugaringEnabled = enableL8
    }
    buildTypes {
        release {
            if (enableDexlayoutOptimizer) {
                logger.warn("Dex Layout Optimizer was enabled")
            }
            experimentalProperties[
                "android.experimental.r8.dex-startup-optimization"
            ] = enableDexlayoutOptimizer

            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                file("proguard-rules.pro")
            )
        }
    }

    testFixtures {
        enable = true
    }

    flavorDimensions.add("default")
    productFlavors {
        register("develop")
        register("beta")
        register("staging")
        register("production")
    }
}

dependencies {
    if (enableL8) {
        coreLibraryDesugaring(libs.desugar.jdk.libs)
    }

    if (enableBaselineProfileGenerator) {
       add("baselineProfile", project(":tests:baselineprofile"))
    }

    runtimeOnly(androidxLibs.profileinstaller)

    implementation(libs.kotlin.stdlib.jdk7)
    implementation(androidxLibs.annotation)
    implementation(androidxLibs.activity.ktx)
    implementation(androidxLibs.appcompat)
    implementation(androidxLibs.core.ktx)
    implementation(androidxLibs.constraintlayout)
    implementation(androidxLibs.fragment.ktx)
    implementation(androidxLibs.lifecycle.livedata.ktx)
    implementation(androidxLibs.lifecycle.viewmodel.ktx)
    implementation(androidxLibs.room.ktx)
    implementation(androidxLibs.room.runtime)
}

if (enableBaselineProfileGenerator) {
    extensions.configure<BaselineProfileConsumerExtension> {
        variants {
            register("release") {
                automaticGenerationDuringBuild = true
            }
        }
        warnings.disabledVariants = false
        warnings.maxAgpVersion = false
    }
}
