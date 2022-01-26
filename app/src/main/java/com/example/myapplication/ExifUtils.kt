package com.example.myapplication

import androidx.exifinterface.media.ExifInterface
import java.io.File
import java.io.IOException

object ExifUtils {
    fun removeLocationData(file: File): Boolean {
        val exif = ExifInterface(file)
        PRIVATE_SENSITIVE_EXIF_TAGS.forEach {
            exif.setAttribute(it, null)
        }
        return try {
            exif.saveAttributes()
            true
        } catch (ex: IOException) {
            android.util.Log.w("ExifInterface", "exif was not stored", ex)
            false
        }
    }

    private val PRIVATE_SENSITIVE_EXIF_TAGS = listOf(
        ExifInterface.TAG_GPS_ALTITUDE,
        ExifInterface.TAG_GPS_LATITUDE,
        ExifInterface.TAG_GPS_LONGITUDE,
    )
}