package com.example.database.impl

import androidx.room.Dao
import androidx.room.Query
import com.example.database.UserEntity
import com.example.database.UserEntity.Companion.AUTO_SAVE_ENTITY_ID
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query(
        "SELECT * FROM user " +
            "WHERE id != $AUTO_SAVE_ENTITY_ID " +
            "ORDER BY save_time_ms DESC",
    )
    fun getUserEntityListFlow(): Flow<List<UserEntity>>

    @Query("SELECT * FROM user WHERE id != $AUTO_SAVE_ENTITY_ID")
    suspend fun getUserEntityList(): List<UserEntity>
}
