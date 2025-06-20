plugins {
    alias(libs.plugins.android.fused.lib)
    id("maven-publish")
}

@Suppress("UnstableApiUsage")
androidFusedLibrary {
     namespace = "com.example.fused.dist"
     minSdk = 28
}

dependencies {
    include(project(":lib:hostconfig"))
}
