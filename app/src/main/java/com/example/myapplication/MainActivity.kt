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
        // By https://android.googlesource.com/platform/frameworks/data-binding/+/8fc33019
        // child1binding.insideChild points
        // Until AGP 4.2, https://github.com/ganadist/VersionCodeDemo/blob/af8d87c5/app/src/main/res/layout/child_outside.xml#L7
        // Since AGP 7.0, https://github.com/ganadist/VersionCodeDemo/blob/af8d87c5/app/src/main/res/layout/child_inside.xml#L7

        val child2binding = ChildInsideBinding.bind(inside.findViewById(R.id.inside_child))
        // child2binding.insideChild points
        // Until AGP 4.2, https://github.com/ganadist/VersionCodeDemo/blob/af8d87c5/app/src/main/res/layout/child_inside.xml#L7
        // Since AGP 7.0, raise NullPointerException when call bind()

        println(child1binding)
        println(child2binding)
    }
}
