package com.example.vigil.ui.viewmodels

import android.content.Context
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vigil.data.database.CreatorStats
import com.example.vigil.data.database.DailyStats
import com.example.vigil.data.repository.ReelRepository
import com.example.vigil.service.AccessibilityTrackingService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: ReelRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

    val todayStats: StateFlow<DailyStats?> = repository.getDailyStats(today)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val isAccessibilityEnabled: StateFlow<Boolean> = flow {
        while (true) {
            val enabled = isAccessibilityServiceEnabled(context, AccessibilityTrackingService::class.java)
            emit(enabled)
            delay(2000) // Check every 2 seconds
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val topCreators: StateFlow<List<CreatorStats>> = repository.getTopCreators()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /**
     * Reliable way to check if an Accessibility Service is enabled.
     */
    private fun isAccessibilityServiceEnabled(context: Context, service: Class<*>): Boolean {
        val expectedComponentName = "${context.packageName}/${service.name}"
        val enabledServicesSetting = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false

        val colonSplitter = TextUtils.SimpleStringSplitter(':')
        colonSplitter.setString(enabledServicesSetting)

        while (colonSplitter.hasNext()) {
            val componentName = colonSplitter.next()
            if (componentName.equals(expectedComponentName, ignoreCase = true)) {
                return true
            }
        }

        Log.d("DashboardViewModel", "Service $expectedComponentName not found in: $enabledServicesSetting")
        return false
    }

    val weeklyStats: StateFlow<List<DailyStats>> = repository.getAllStats()
        .map { allStats ->
            // Filter last 7 days
            allStats.take(7)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
