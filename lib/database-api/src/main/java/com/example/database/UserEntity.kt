package com.example.database

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val uid: Int,
    @ColumnInfo(name = "subtype") val subtype: UserEntitySubtype,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?,
    @Embedded val userExtra: UserExtra?,
) {
    companion object {
        const val AUTO_SAVE_ENTITY_ID = -1L
    }

    enum class UserEntitySubtype {
        DRAFT,
        AUTO_SAVE,
    }
}

// This must be not nested class to keep compatibility with Room KSP2
// https://issuetracker.google.com/issues/447154195#comment10
data class UserExtra(
    @ColumnInfo(name = "thumbnail") val thumbnail: String?,
    @ColumnInfo(name = "age") val age: Long,
    @ColumnInfo(name = "save_time_ms") val saveTimeMs: Long,
)
