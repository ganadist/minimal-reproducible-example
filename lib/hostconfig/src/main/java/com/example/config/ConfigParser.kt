package com.example.config

import androidx.annotation.Keep

@Keep
interface ConfigParser<T : Any> {
    fun parse(
        value: String?,
        fallback: T?,
    ): T?
}
