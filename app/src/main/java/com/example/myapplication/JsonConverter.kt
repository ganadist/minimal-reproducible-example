package com.example.myapplication

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class JsonConverter {
    fun create(): Moshi =
        Moshi.Builder()
            .add(
                PolymorphicJsonAdapterFactory.of(Shape::class.java, "shape")
                    .withSubtype(Shape.Circle::class.java, "circle")
                    .withSubtype(Shape.Square::class.java, "square")
            )
            .add(KotlinJsonAdapterFactory())
            .build()
}