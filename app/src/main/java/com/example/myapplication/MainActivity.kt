package com.example.myapplication

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    companion object {
        // com.myapplication.feature.impl.R is referenced with runtimeOnly
        @StringRes
        private val FEATURE_STRING_RESID = com.myapplication.feature.impl.R.string.feature_name
    }
}
