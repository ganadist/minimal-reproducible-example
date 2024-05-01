package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

@Suppress("DEPRECATION")
private typealias DeprecatedLocalBroadcastManager =
    androidx.localbroadcastmanager.content.LocalBroadcastManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        @Suppress("TYPEALIAS_EXPANSION_DEPRECATION")
        DeprecatedLocalBroadcastManager.getInstance(this)
            .sendBroadcast(android.content.Intent("com.example.myapplication"))

    }
}
