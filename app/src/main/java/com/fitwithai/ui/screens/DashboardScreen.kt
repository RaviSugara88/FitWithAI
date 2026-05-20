package com.fitwithai.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fitwithai.navigation.Routes

data class DashboardTab(
    val route: String,
    val label: String,
    val icon: @Composable () -> Unit,
)

@Composable
fun DashboardScreen() {
    val dashboardNavController = rememberNavController()
    val tabs = listOf(
        DashboardTab(
            route = Routes.HOME,
            label = "Home",
            icon = { Icon(imageVector = Icons.Filled.Home, contentDescription = "Home") },
        ),
        DashboardTab(
            route = Routes.ACTIVITY,
            label = "Activity",
            icon = { Icon(imageVector = Icons.Filled.FitnessCenter, contentDescription = "Activity") },
        ),
        DashboardTab(
            route = Routes.HISTORY,
            label = "History",
            icon = { Icon(imageVector = Icons.Filled.BarChart, contentDescription = "History") },
        ),
        DashboardTab(
            route = Routes.PROFILE,
            label = "Profile",
            icon = { Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = "Profile") },
        ),
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
                        icon = tab.icon,
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
            composable(Routes.ACTIVITY) { ActivityScreen() }
            composable(Routes.HISTORY) { HistoryScreen() }
            composable(Routes.PROFILE) { ProfileScreen() }
        }
    }
}
