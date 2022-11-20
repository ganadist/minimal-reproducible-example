package com.example.myapplication.hostconfig

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView

class TestView(context: Context) : LinearLayout(context) {
    fun inflateTextView() {
        getTextView(
            com.example.myapplication.hostconfig.feature1.userinterface.resources.R.layout.hostconfig_main // ktlint-disable max-line-length
        )
            .setOnClickListener {
                println("hello")
            }
    }

    fun getTextView(resId: Int): TextView {
        val inflater = LayoutInflater.from(context)
        return inflater.inflate(resId, this) as TextView
    }

    enum class Dummy(val type: String) {
        // empty
    }
}
