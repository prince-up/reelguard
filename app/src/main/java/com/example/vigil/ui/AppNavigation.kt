package com.example.vigil.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vigil.ui.screens.DashboardScreen
import com.example.vigil.ui.screens.DebugScreen
import com.example.vigil.ui.screens.DeveloperProfileScreen
import com.example.vigil.ui.screens.OnboardingScreen

private const val TAG = "NAVIGATION_DEBUG"

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "onboarding") {
        composable("onboarding") {
            OnboardingScreen(onNavigateToDashboard = {
                safeNavigate(navController, "dashboard") {
                    popUpTo("onboarding") { inclusive = true }
                }
            })
        }
        composable("dashboard") {
            DashboardScreen(
                onNavigateToDeveloper = {
                    safeNavigate(navController, "developer")
                },
                onNavigateToDebug = {
                    safeNavigate(navController, "debug")
                }
            )
        }
        composable("developer") {
            DeveloperProfileScreen(onBack = {
                navController.popBackStack()
            })
        }
        composable("debug") {
            DebugScreen(onBack = {
                navController.popBackStack()
            })
        }
    }
}

/**
 * Enhanced crash-safe navigation wrapper
 */
fun safeNavigate(
    navController: NavHostController,
    route: String,
    builder: androidx.navigation.NavOptionsBuilder.() -> Unit = {}
) {
    try {
        Log.d(TAG, "Attempting to navigate to: $route")
        
        // Check if the route is already the current destination to avoid redundant navigations
        if (navController.currentDestination?.route == route) {
            Log.w(TAG, "Already at destination: $route. Skipping.")
            return
        }

        navController.navigate(route, builder)
        Log.d(TAG, "Navigation to $route successful")
    } catch (e: Exception) {
        Log.e(TAG, "CRITICAL: Navigation to $route failed!", e)
    }
}
