package com.fitwithai.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fitwithai.test_mvi.CounterScreen
import com.fitwithai.test_mvi.CounterViewModel
import com.fitwithai.ui.navigation.AppNavGraph
import com.fitwithai.ui.screens.DashboardScreen
import com.fitwithai.ui.screens.profile.screen.ProfileScreen
import com.fitwithai.ui.screens.profile.viewmodel.ProfileViewModel

class MainActivity : ComponentActivity() {
    private val counterViewModel: ProfileViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProfileScreen(viewModel = counterViewModel)
           // DashboardScreen()
            // Dipti
            // FixCost -
           /* AppNavGraph(
                onGoogleLogin = { onSuccess -> onSuccess() },
                onInstagramLogin = { onSuccess -> onSuccess() },
            )*/

        }
    }
}
