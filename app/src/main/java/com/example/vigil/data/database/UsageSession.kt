package com.example.vigil.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usage_sessions")
data class UsageSession(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val startTime: Long,
    val endTime: Long,
    val duration: Long
)
