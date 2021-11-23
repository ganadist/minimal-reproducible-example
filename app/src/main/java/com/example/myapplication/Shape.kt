package com.example.myapplication

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
sealed class Shape(
    @Json(name = "shape")
    open val shape: String,
    @Json(name = "area")
    open val area: String
) {
    data class Circle(
        @Json(name = "shape")
        override val shape: String,
        @Json(name = "area")
        override val area: String,
    ) : Shape(shape, area)

    data class Square(
        @Json(name = "shape")
        override val shape: String,
        @Json(name = "area")
        override val area: String,
    ) : Shape(shape, area)
}