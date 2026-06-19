package com.example.vigil.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.vigil.service.AccessibilityTrackingService
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vigil.ui.viewmodels.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebugScreen(
    onBack: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val isConnected by AccessibilityTrackingService.isConnected.collectAsState()
    val eventCount by AccessibilityTrackingService.eventCount.collectAsState()
    val lastPackage by AccessibilityTrackingService.lastPackage.collectAsState()
    val lastType by AccessibilityTrackingService.lastType.collectAsState()
    val lastUser by AccessibilityTrackingService.lastUsernameDebug.collectAsState()
    val todayStats by viewModel.todayStats.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("System Diagnostics") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            item {
                DebugCard(
                    title = "Service Connection",
                    value = if (isConnected) "CONNECTED" else "DISCONNECTED",
                    color = if (isConnected) Color(0xFF2E7D32) else Color(0xFFC62828)
                )
            }
            item { Spacer(modifier = Modifier.height(12.dp)) }
            item {
                DebugCard(title = "Events Received", value = eventCount.toString())
            }
            item { Spacer(modifier = Modifier.height(12.dp)) }
            item {
                DebugCard(title = "Last Package", value = lastPackage)
            }
            item { Spacer(modifier = Modifier.height(12.dp)) }
            item {
                DebugCard(title = "Last Event Type", value = lastType)
            }
            item { Spacer(modifier = Modifier.height(12.dp)) }
            item {
                DebugCard(title = "Last Detected User", value = lastUser)
            }
            item { Spacer(modifier = Modifier.height(12.dp)) }
            item {
                DebugCard(title = "Database Reel Count", value = (todayStats?.reelCount ?: 0).toString())
            }
            
            item { Spacer(modifier = Modifier.height(24.dp)) }
            
            item {
                if (eventCount == 0 && isConnected) {
                    WarningBox(text = "CRITICAL: Service is connected but receiving 0 events. This usually means Android has suspended the service. Try: Settings -> Accessibility -> Turn OFF and ON again.")
                }
            }
        }
    }
}

@Composable
fun DebugCard(title: String, value: String, color: Color = MaterialTheme.colorScheme.onSurface) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
            Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
fun WarningBox(text: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.BugReport, contentDescription = null, tint = Color(0xFFC62828))
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = text, style = MaterialTheme.typography.bodySmall, color = Color(0xFFC62828))
        }
    }
}
