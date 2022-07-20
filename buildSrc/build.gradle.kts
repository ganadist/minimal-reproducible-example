plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(libs.android.gradle)
    implementation(libs.kotlin.gradle)
    // workaround for https://github.com/Kotlin/dokka/issues/2452
    implementation(libs.kotlin.dokka.core)
}
