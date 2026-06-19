package com.example.vigil.data.repository

import android.util.Log
import com.example.vigil.data.database.CreatorStats
import com.example.vigil.data.database.DailyStats
import com.example.vigil.data.database.ReelDao
import com.example.vigil.data.database.UsageSession
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReelRepository @Inject constructor(
    private val reelDao: ReelDao
) {
    private val TAG = "DB_DEBUG"

    fun getDailyStats(date: String): Flow<DailyStats?> = reelDao.getDailyStats(date)

    suspend fun incrementReelCount(date: String) {
        // Step 1: Ensure the row exists without overwriting existing data
        val initialStats = DailyStats(date = date, reelCount = 0, watchTimeMillis = 0)
        reelDao.insertDailyStatsIfNotExists(initialStats)
        
        // Step 2: Diagnostic Log - Before
        val before = reelDao.getDailyStatsSync(date)
        Log.d(TAG, "DB_BEFORE_INCREMENT: date=$date, count=${before?.reelCount}")

        // Step 3: Increment
        reelDao.incrementReelCount(date)

        // Step 4: Diagnostic Log - After
        val after = reelDao.getDailyStatsSync(date)
        Log.d(TAG, "DB_AFTER_INCREMENT: date=$date, count=${after?.reelCount}")
    }

    suspend fun incrementCreatorCount(username: String) {
        val creator = CreatorStats(username = username)
        reelDao.insertCreator(creator)
        reelDao.incrementCreatorCount(username)
        Log.d("CREATOR_ANALYTICS", "Creator detected: $username")
    }

    fun getTopCreators(): Flow<List<CreatorStats>> = reelDao.getTopCreators()

    suspend fun addWatchTime(date: String, millis: Long) {
        val stats = DailyStats(date = date)
        reelDao.insertDailyStatsIfNotExists(stats)
        reelDao.addWatchTime(date, millis)
    }

    fun getAllStats(): Flow<List<DailyStats>> = reelDao.getAllStats()

    suspend fun insertSession(session: UsageSession) = reelDao.insertSession(session)

    fun getAllSessions(): Flow<List<UsageSession>> = reelDao.getAllSessions()
}
