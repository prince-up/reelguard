package com.example.vigil.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReelDao {
    @Query("SELECT * FROM daily_stats WHERE date = :date")
    fun getDailyStats(date: String): Flow<DailyStats?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDailyStatsIfNotExists(stats: DailyStats)

    @Query("SELECT * FROM daily_stats WHERE date = :date")
    suspend fun getDailyStatsSync(date: String): DailyStats?

    @Query("UPDATE daily_stats SET reelCount = reelCount + 1 WHERE date = :date")
    suspend fun incrementReelCount(date: String)

    @Query("UPDATE daily_stats SET watchTimeMillis = watchTimeMillis + :millis WHERE date = :date")
    suspend fun addWatchTime(date: String, millis: Long)

    @Query("SELECT * FROM daily_stats ORDER BY date DESC")
    fun getAllStats(): Flow<List<DailyStats>>

    @Insert
    suspend fun insertSession(session: UsageSession)

    @Query("SELECT * FROM usage_sessions ORDER BY startTime DESC")
    fun getAllSessions(): Flow<List<UsageSession>>

    // --- Creator Analytics ---
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCreator(creator: CreatorStats)

    @Query("UPDATE creator_stats SET watchCount = watchCount + 1 WHERE username = :username")
    suspend fun incrementCreatorCount(username: String)

    @Query("SELECT * FROM creator_stats ORDER BY watchCount DESC LIMIT 10")
    fun getTopCreators(): Flow<List<CreatorStats>>
}
