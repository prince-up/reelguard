package com.example.vigil.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

import com.example.vigil.data.database.CreatorStats

@Database(entities = [DailyStats::class, UsageSession::class, CreatorStats::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reelDao(): ReelDao
}
