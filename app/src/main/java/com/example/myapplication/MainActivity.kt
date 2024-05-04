package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.IdRes

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // https://issuetracker.google.com/issues/209843426
    @Suppress("WRONG_ANNOTATION_TARGET")
    private val RESID_MAP = mapOf<Int, @IdRes Int>(
        1 to R.id.text_view,
    )
}
