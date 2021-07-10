package com.example.dynamicfeature1

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.TextView
import androidx.room.util.DBUtil
import com.example.dynamicfeature1.TestActivity
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith( RobolectricTestRunner::class)
class SQLiteTest {
    @Test
    fun test_sqlite() {
        val db = SQLiteDatabase.createInMemory(SQLiteDatabase.OpenParams.Builder().build())
        println(db.path)
    }
}
