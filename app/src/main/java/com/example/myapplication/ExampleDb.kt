package com.example.myapplication

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ExampleModel::class],
    version = 1,
    exportSchema = false
)
abstract class ExampleDb : RoomDatabase() {
    abstract fun exampleDao(): ExampleDao
}
