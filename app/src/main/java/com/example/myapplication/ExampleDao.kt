package com.example.myapplication

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import java.util.concurrent.TimeUnit

@Dao
interface ExampleDao {

	/*
    @Query("DELETE FROM table WHERE id = :id")
    fun deleteItem(id: String)

    @Delete
    fun deleteItemList(list: List<ExampleModel>)

    @Query("DELETE FROM table WHERE createdTime <= :expireTime")
    fun deleteExpired(expireTime: Long = getExpireTime())
    */

    private fun getExpireTime(): Long = System.currentTimeMillis() - VALID_DURATION

    companion object {
        private val VALID_DURATION: Long = TimeUnit.DAYS.toMillis(1)
    }
}
