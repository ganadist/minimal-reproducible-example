plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.android.gradle)
    implementation(libs.kotlin.gradle)
    compileOnly(libs.ksp.gradle)
    compileOnly(libs.gradle.test.retry)
}

gradlePlugin {
    plugins {
        register("androidBuilder") {
            id = "com.myapplication.android.builder"
            implementationClass = "AndroidBuilderPlugin"
        }
    }
}
