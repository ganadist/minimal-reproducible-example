// These values are loaded by gradle/build_constant.gradle.kts from settings.gradle.kts
val versionCode: Int by gradle.extra
val versionName: String by gradle.extra

println("kts: versionCode = $versionCode")
println("kts: versionName = $versionName")
