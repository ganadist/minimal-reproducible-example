package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.example.myapplication.databinding.ChildInsideBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val child1 = findViewById<LinearLayout>(R.id.child1)
        val child2 = findViewById<LinearLayout>(R.id.child2)

        val outside: View = layoutInflater.inflate(R.layout.child_outside, child1)
        val inside: View = layoutInflater.inflate(R.layout.child_inside, child2)

        val child1binding = ChildInsideBinding.bind(outside.findViewById(R.id.inside_child))
        val child2binding = ChildInsideBinding.bind(inside.findViewById(R.id.inside_child))

        println(child1binding)
        println(child2binding)
    }
}
