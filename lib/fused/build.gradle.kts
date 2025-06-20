plugins {
    alias(libs.plugins.android.fused.lib)
    id("maven-publish")
}

@Suppress("UnstableApiUsage")
androidFusedLibrary {
     namespace = "com.example.fused.dist"
     // lower than project(":lib:hostconfig")
     minSdk = 26
}

dependencies {
    include(project(":lib:hostconfig"))
}
