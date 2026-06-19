package com.example.vigil.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.example.vigil.data.repository.ReelRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class AccessibilityTrackingService : AccessibilityService() {

    @Inject
    lateinit var repository: ReelRepository

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var lastIncrementTime: Long = 0

    companion object {
        private const val TAG = "REEL_DEBUG"
        private const val SCROLL_DEBOUNCE_MS = 700L
        
        private val _isConnected = MutableStateFlow(false)
        val isConnected = _isConnected.asStateFlow()

        private val _eventCount = MutableStateFlow(0)
        val eventCount = _eventCount.asStateFlow()

        private val _lastPackage = MutableStateFlow("None")
        val lastPackage = _lastPackage.asStateFlow()

        private val _lastType = MutableStateFlow("None")
        val lastType = _lastType.asStateFlow()

        private val _lastUsername = MutableStateFlow("None")
        val lastUsernameDebug = _lastUsername.asStateFlow()
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        _isConnected.value = true
        Log.e(TAG, "onServiceConnected: SERVICE CONNECTED")

        serviceInfo = serviceInfo.apply {
            eventTypes = AccessibilityEvent.TYPES_ALL_MASK
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            flags = AccessibilityServiceInfo.DEFAULT or 
                    AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS or 
                    AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
            packageNames = arrayOf("com.instagram.android")
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        val eventType = event.eventType
        val packageName = event.packageName?.toString() ?: ""

        _eventCount.value++
        _lastPackage.value = packageName
        _lastType.value = AccessibilityEvent.eventTypeToString(eventType)

        if (packageName != "com.instagram.android") return

        // Diagnostic log for every Instagram event
        Log.v(TAG, "EVENT_RECEIVED: type=${_lastType.value}")

        // 1. REEL COUNTING: ONLY on TYPE_VIEW_SCROLLED
        if (eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED) {
            val currentTime = System.currentTimeMillis()
            
            if (currentTime - lastIncrementTime > SCROLL_DEBOUNCE_MS) {
                Log.d(TAG, "SCROLL_DETECTED")
                Log.e(TAG, "REEL_COUNT_INCREMENTED")
                
                lastIncrementTime = currentTime
                incrementReelCountOnly()
            }
        }

        // 2. CREATOR ANALYTICS: Use CONTENT_CHANGED and others to keep username updated
        // This runs independently of the scroll-based counter
        if (eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED || 
            eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED) {
            
            val rootNode = rootInActiveWindow
            if (rootNode != null) {
                val creator = extractCreator(rootNode)
                if (creator != null) {
                    Log.d("CREATOR_ANALYTICS", "CREATOR_DETECTED: $creator")
                    _lastUsername.value = creator
                    updateCreatorStats(creator)
                }
            }
        }
    }

    private fun extractCreator(node: AccessibilityNodeInfo): String? {
        // Method 1: ID-based
        val viewerTitleNodes = node.findAccessibilityNodeInfosByViewId("com.instagram.android:id/reel_viewer_title")
        if (viewerTitleNodes.isNotEmpty()) {
            return viewerTitleNodes[0].text?.toString()
        }

        // Method 2: Recursive text scan
        val texts = mutableListOf<String>()
        scanTexts(node, texts)
        
        return texts.firstOrNull { it.length in 3..30 && !it.contains(" ") && !it.contains("@") && !it.contains("/") }
    }

    private fun scanTexts(node: AccessibilityNodeInfo, list: MutableList<String>) {
        node.text?.let { list.add(it.toString()) }
        for (i in 0 until node.childCount) {
            val child = node.getChild(i)
            if (child != null) {
                scanTexts(child, list)
                child.recycle()
            }
        }
    }

    private fun incrementReelCountOnly() {
        val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        serviceScope.launch {
            repository.incrementReelCount(today)
        }
    }

    private fun updateCreatorStats(creator: String) {
        serviceScope.launch {
            repository.incrementCreatorCount(creator)
        }
    }

    override fun onInterrupt() {
        _isConnected.value = false
    }

    override fun onDestroy() {
        super.onDestroy()
        _isConnected.value = false
    }
}
