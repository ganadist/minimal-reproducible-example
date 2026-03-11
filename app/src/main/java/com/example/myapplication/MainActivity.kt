package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.config.Builder
import com.example.config.UserConfiguration

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val user: UserConfiguration =
            Builder.from(
                mapOf(
                    "userId" to "[ { \"userId\": \"0000\" } ]",
                ),
            )
        Log.d("MainActivity", "userid = ${user.userId}")
        assert(user.userId == "0000")
    }
}
