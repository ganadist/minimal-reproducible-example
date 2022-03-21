package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(MainActivity::class.java.getSimpleName(), "Custom Log: onCreate")
        android.util.Log.d(MainActivity::class.java.getSimpleName(), "System Log: onCreate")
        Log.v(MainActivity::class.java.getSimpleName(), "Custom Log: onCreate")
        android.util.Log.v(MainActivity::class.java.getSimpleName(), "System Log: onCreate")
    }
}
