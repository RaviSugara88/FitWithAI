package com.fitwithai.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    onGoogleLogin: (onSuccess: () -> Unit) -> Unit,
    onInstagramLogin: (onSuccess: () -> Unit) -> Unit,
    onLoginSuccess: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Login",
                style = MaterialTheme.typography.headlineMedium,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Sign in with Google or Instagram",
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    onGoogleLogin(onLoginSuccess)
                },
                modifier = Modifier.fillMaxSize(fraction = 0.7f),
            ) {
                Text(text = "Continue with Google")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    onInstagramLogin(onLoginSuccess)
                },
                modifier = Modifier.fillMaxSize(fraction = 0.7f),
            ) {
                Text(text = "Continue with Instagram")
            }
        }
    }
}
