package com.example.vigil.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "daily_stats")
data class DailyStats(
    @PrimaryKey val date: String, // ISO-8601 date string
    val reelCount: Int = 0,
    val watchTimeMillis: Long = 0
)
