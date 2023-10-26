import com.android.build.api.dsl.ManagedVirtualDevice

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.test)
    alias(libs.plugins.kotlin.android)
    alias(androidxLibs.plugins.baselineprofile)
}

android {
    namespace = "com.example.baselineprofile"

    targetProjectPath = ":app"

    flavorDimensions += listOf("default")
    productFlavors {
        create("staging") { dimension = "default" }
        create("production") { dimension = "default" }
    }

    testOptions.managedDevices.devices {
        create<ManagedVirtualDevice>("pixel6Api34") {
            device = "Pixel 6"
            apiLevel = 34
            systemImageSource = "google"
        }
    }
}

// This is the configuration block for the Baseline Profile plugin.
// You can specify to run the generators on a managed devices or connected devices.
baselineProfile {
    managedDevices += "pixel6Api34"
    useConnectedDevices = false
}

dependencies {
    // basic dependencies will be loaded by build-logic
    implementation(androidxLibs.benchmark.macro.junit4)
}
