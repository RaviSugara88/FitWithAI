package com.fitwithai.ui.screens.dashboard

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fitwithai.navigation.Routes
import com.fitwithai.ui.screens.ai_coach.AiCoachScreen
import com.fitwithai.ui.screens.home.HomeScreen
import com.fitwithai.ui.screens.profile.ProfileTabScreen
import com.fitwithai.ui.screens.progress.ProgressScreen
import com.fitwithai.ui.screens.workout.WorkoutScreen

private data class DashboardTab(
    val route: String,
    val label: String,
    val imageVector: ImageVector,
)

@Composable
fun DashboardScreen() {
    val dashboardNavController = rememberNavController()
    val tabs = listOf(
        DashboardTab(Routes.HOME, "Home", Icons.Filled.Home),
        DashboardTab(Routes.ACTIVITY, "Workout", Icons.Filled.FitnessCenter),
        DashboardTab(Routes.HISTORY, "AI Coach", Icons.Filled.BarChart),
        DashboardTab(Routes.PROGRESS, "Progress", Icons.Filled.ShowChart),
        DashboardTab(Routes.PROFILE, "Profile", Icons.Filled.AccountCircle),
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by dashboardNavController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                tabs.forEach { tab ->
                    val selected = currentDestination?.hierarchy?.any { it.route == tab.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            dashboardNavController.navigate(tab.route) {
                                popUpTo(dashboardNavController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = tab.imageVector,
                                contentDescription = tab.label,
                            )
                        },
                        label = { Text(text = tab.label) },
                    )
                }
            }
        },
    ) { padding ->
        NavHost(
            navController = dashboardNavController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(padding),
        ) {
            composable(Routes.HOME) { HomeScreen() }
            composable(Routes.ACTIVITY) { WorkoutScreen() }
            composable(Routes.HISTORY) { AiCoachScreen() }
            composable(Routes.PROGRESS) { ProgressScreen() }
            composable(Routes.PROFILE) { ProfileTabScreen() }
        }
    }
}
