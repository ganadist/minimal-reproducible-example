package com.example.myapplication

import android.content.ComponentName
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val startupIntent = Intent()
        startupIntent.component = ComponentName(this, "com.example.dynamicfeature1.TestActivity")
        startupIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(startupIntent)
    }
}
