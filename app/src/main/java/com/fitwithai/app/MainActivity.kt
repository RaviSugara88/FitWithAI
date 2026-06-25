package com.fitwithai.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.fitwithai.auth.ActivityProvider
import com.fitwithai.ui.navigation.AppNavGraph
import com.fitwithai.ui.theme.FitWithAITheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val activityProvider: ActivityProvider by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        activityProvider.set(this)
        setContent {
            FitWithAITheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavGraph()
                }
            }
        }
    }

    override fun onDestroy() {
        activityProvider.set(null)
        super.onDestroy()
    }
}
