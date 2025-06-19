package com.example.myapplication

import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}

class ColorItem(
    val name: String,
    @param:VisibleForTesting private val color: Int = 0
) {
    override fun toString(): String {
        return name
    }
}
