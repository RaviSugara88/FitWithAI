package com.fitwithai.domain.repository

import android.app.Activity
import com.fitwithai.domain.model.AuthResult

interface InstagramLoginRepository {
    suspend fun signInWithInstagram(activity: Activity): AuthResult
}
