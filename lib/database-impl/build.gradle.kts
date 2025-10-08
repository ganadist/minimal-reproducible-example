plugins {
    alias(libs.plugins.android.lib)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(androidxLibs.plugins.room)
}

android {
    namespace = "com.example.database.impl"
}

room {
    schemaDirectory("src/schemas")
}

dependencies {
    implementation(project(":lib:database-api"))
    ksp(androidxLibs.room.compiler)
    implementation(androidxLibs.room.ktx)
}
