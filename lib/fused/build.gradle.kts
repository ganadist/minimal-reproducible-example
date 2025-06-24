plugins {
    alias(libs.plugins.android.fused.lib)
    id("maven-publish")
}

@Suppress("UnstableApiUsage")
androidFusedLibrary {
     namespace = "com.example.fused.dist"
     // Need to sync with minSdk of :lib:fused-overrides
     minSdk = 26
}

dependencies {
    // need to sync namespaces of dependencies with "tools:overrideLibrary" in :lib:fused-overrides
    include(project(":lib:fused-overrides"))

    include(project(":lib:hostconfig"))
}
