package com.example.config

import kotlin.reflect.KClass

class ConfigAnnotation {
    @Target(AnnotationTarget.FIELD)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Key(
        val name: String,
        val customParser: KClass<out ConfigParser<out Any>> =
            Unspecified::class,
    )

    @Target(AnnotationTarget.FIELD)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Category
}

internal class Unspecified private constructor() : ConfigParser<Any> {
    override fun parse(
        value: String?,
        fallback: Any?,
    ): Any? = null
}
