package com.example.vigil.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint

/**
 * OverlayService is temporarily disabled to prevent crashes related to ViewTreeLifecycleOwner.
 * All UI and WindowManager logic has been removed.
 */
@AndroidEntryPoint
class OverlayService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        Log.d("OVERLAY", "Overlay disabled")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Return START_NOT_STICKY since the service has no active work
        return START_NOT_STICKY
    }
}
