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
    }
}
