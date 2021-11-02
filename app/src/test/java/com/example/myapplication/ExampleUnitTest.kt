package com.example.myapplication

import android.graphics.BitmapFactory
import org.apache.commons.io.IOUtils
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(RobolectricTestRunner::class)
class ExampleUnitTest {

    @Test
    fun decode_png() {
        val istream = ClassLoader.getSystemClassLoader().getResourceAsStream("archlinux-logo.png")
        val tmp = File.createTempFile("test", "image")
        val output = BufferedOutputStream(FileOutputStream(tmp))

        IOUtils.copy(istream, output)

        BitmapFactory.decodeFile(tmp.absolutePath)
    }
}
