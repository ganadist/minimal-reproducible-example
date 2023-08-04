package com.example.myapplication

import java.io.Serializable

data class EditData @JvmOverloads constructor(
    var value1: Long = 1L,
    var value2: Boolean = false,
): Serializable {
    companion object {
        const val DEFAULT_VALUE = -1
    }
}
