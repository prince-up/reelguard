package com.example.vigil.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "creator_stats")
data class CreatorStats(
    @PrimaryKey
    val username: String,
    val watchCount: Int = 0
)
