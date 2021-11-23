package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.squareup.moshi.Moshi

inline fun <reified T> Moshi.fromJson(json: String): T? = adapter<T>(T::class.java).fromJson(json)

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val jsonString = resources.getString(R.string.json_value)
        val converter = JsonConverter().create()
        val result = converter.fromJson<Shape>(jsonString)
        Log.e("MainActivity", result.toString())
    }
}