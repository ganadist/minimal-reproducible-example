package com.example.myapplication

import android.content.Context
import androidx.exifinterface.media.ExifInterface
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.apache.commons.codec.digest.DigestUtils
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption

@RunWith(AndroidJUnit4::class)
class ExampleUnitTest {
    private val targetFile = File("test_asset")

    @Before
    fun setUp() {
        ClassLoader.getSystemClassLoader().getResourceAsStream("test_asset").use {
            Files.copy(it, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        }
    }

    private fun getChecksum(file: File): String {
        Files.newInputStream(file.toPath()).use {
            return DigestUtils.md5Hex(it)
        }
    }

    @Test
    fun saveExif() {
        val md5sum1 = getChecksum(targetFile)
        ExifUtils.removeLocationData(targetFile)

        val md5sum2 = getChecksum(targetFile)
        Assert.assertEquals("checksum was changed", md5sum1, md5sum2)

    }
}
