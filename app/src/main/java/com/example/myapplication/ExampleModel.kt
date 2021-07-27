package com.example.myapplication

import androidx.room.Entity
import androidx.room.Index
import java.io.Serializable

@Entity(
    tableName = "table",
    indices = [Index("id")],
    primaryKeys = ["id"]
)
data class ExampleModel(
    val id: String,
    val createdTime: Long
): Serializable {
    companion object { 
        private const val serialVersionUID: Long = 0L
    }
}
