plugins {
    alias(libs.plugins.android.lib)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(androidxLibs.plugins.room)
}

android {
    namespace = "com.example.database"
}

room {
    schemaDirectory("src/schemas")
}

dependencies {
    ksp(androidxLibs.room.compiler)
    implementation(androidxLibs.room.ktx)
}
