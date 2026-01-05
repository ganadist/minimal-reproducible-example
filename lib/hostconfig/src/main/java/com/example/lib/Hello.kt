package com.example.lib

class Hello {
    private fun CharSequence.toString(toPretty: Boolean): String = if (toPretty) {
        "Hello $this World"
    } else {
        this.toString()
    }

    fun greet(name: String, toPretty: Boolean = false): String {
        return name.toString(toPretty)
    }
}
