package com.example.vigil.util

import android.content.Context
import com.example.vigil.data.database.DailyStats
import java.io.File

object DataExportUtil {
    fun exportToCsv(context: Context, stats: List<DailyStats>): File {
        val file = File(context.cacheDir, "reel_stats.csv")
        file.writeText("Date,Reel Count,Watch Time (ms)\n")
        stats.forEach {
            file.appendText("${it.date},${it.reelCount},${it.watchTimeMillis}\n")
        }
        return file
    }
}
