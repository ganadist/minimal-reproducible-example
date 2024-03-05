package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pref = getSharedPreferences("login", MODE_PRIVATE)
        val login = pref.getBoolean("login", false)
        if (login) {
            // TODO
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
