plugins {
    alias(libs.plugins.android.lib)
}

android {
    namespace = "com.example.hostconfig"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField("String", "HOST", "String.valueOf(\"https://api.example.com\")")
    }
}

dependencies {
    testImplementation(libs.kotlin.test.junit) {
        exclude(group = "junit")
    }
}
