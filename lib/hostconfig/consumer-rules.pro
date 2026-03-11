-keep class * implements com.example.config.ConfigParser

# These fields are loaded by reflection
-keepclassmembers, allowobfuscation class com.example.config.** {
  @com.example.config.ConfigAnnotation$Key <fields>;
}