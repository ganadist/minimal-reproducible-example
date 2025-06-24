plugins {
    alias(libs.plugins.android.lib)
}

android {
    namespace = "com.example.fused.overrides"
    // need to sync minSdk with :lib:fused project
    defaultConfig.minSdk = 26
}
