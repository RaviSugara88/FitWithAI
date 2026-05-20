package com.fitwithai.data.auth

import androidx.activity.ComponentActivity

interface GoogleCredentialProvider {
    suspend fun getGoogleIdToken(activity: ComponentActivity): String
}
