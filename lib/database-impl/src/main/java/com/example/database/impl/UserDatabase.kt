package com.example.database.impl

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.database.UserEntity

@Database(
    entities = [
        UserEntity::class,
    ],
    version = 1,
)
abstract class UserDatabase : RoomDatabase() {
    abstract fun getUserDao(): UserDao
}
