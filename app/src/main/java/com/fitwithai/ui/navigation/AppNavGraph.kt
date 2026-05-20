package com.fitwithai.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fitwithai.navigation.Routes
import com.fitwithai.ui.screens.dashboard.DashboardScreen
import com.fitwithai.ui.screens.login.LoginScreen

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN,
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onNavigateToDashboard = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.LOGIN) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
            )
        }
        composable(Routes.DASHBOARD) {
            DashboardScreen()
        }
    }
}
