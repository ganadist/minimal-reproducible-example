package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            val value: Map<String, String> = requireNotNull(
                savedInstanceState?.getCompatSerializable("ANY")
            )
            println("value = $value")
        } catch (th: Throwable) {
            println("$th")
        }
    }

    val otherValue: Map<String, String> by lazy {
        Bundle().getCompatSerializable("ANY") ?: emptyMap()
    }
}
