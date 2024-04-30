package com.example.myapplication

import android.os.Build
import android.os.Bundle
import java.io.Serializable

inline fun <reified T : Serializable> Bundle.getCompatSerializable(key: String): T? =
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        @Suppress("DEPRECATION")
        getSerializable(key) as? T
    } else {
        getSerializable(key, T::class.java)
    }
