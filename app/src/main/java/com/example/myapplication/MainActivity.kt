package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.io.File
import java.util.Arrays

class MainActivity : AppCompatActivity() {
    private val paths: Array<File> = File(".").listFiles()?.also {
        Arrays.sort(it)
    } ?: throw RuntimeException("here")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
