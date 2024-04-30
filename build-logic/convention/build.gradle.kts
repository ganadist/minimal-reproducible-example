plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.android.gradle)
    implementation(libs.kotlin.gradle)
    compileOnly(libs.ksp.gradle)
    compileOnly(libs.gradle.enterprise)
}

gradlePlugin {
    plugins {
        register("androidBuilder") {
            id = "com.myapplication.android.builder"
            implementationClass = "AndroidBuilderPlugin"
        }
        register("androidVersionCatalogsLoader") {
            id = "com.myapplication.android.versions.loader"
            implementationClass = "AndroidVersionCatalogsLoaderPlugin"
        }
        register("androidVersionCatalogsChecker") {
            id = "com.myapplication.android.versions.checker"
            implementationClass = "AndroidVersionCatalogsCheckerPlugin"
        }
        register("composeK2MigrationPlugin") {
            id = "com.myapplication.compose.k2.migration"
            implementationClass = "ComposeK2MigrationPlugin"
            version = "0.0.1"
        }
    }
}
