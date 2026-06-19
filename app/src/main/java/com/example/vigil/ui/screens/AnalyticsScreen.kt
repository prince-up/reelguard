package com.example.vigil.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.vigil.data.database.DailyStats

@Composable
fun AnalyticsScreen(stats: List<DailyStats>) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Weekly Trend", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        SimpleBarChart(stats)
    }
}

@Composable
fun SimpleBarChart(stats: List<DailyStats>) {
    val maxCount = stats.maxOfOrNull { it.reelCount }?.coerceAtLeast(1) ?: 1
    
    Canvas(modifier = Modifier.fillMaxWidth().height(200.dp)) {
        val barWidth = size.width / 7
        stats.takeLast(7).forEachIndexed { index, stat ->
            val barHeight = (stat.reelCount.toFloat() / maxCount) * size.height
            drawRect(
                color = Color(0xFF6200EE),
                topLeft = Offset(index * barWidth + 4.dp.toPx(), size.height - barHeight),
                size = Size(barWidth - 8.dp.toPx(), barHeight)
            )
        }
    }
}
