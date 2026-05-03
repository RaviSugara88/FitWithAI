package com.fitwithai.domain.repository

import com.fitwithai.domain.model.AuthResult

interface GoogleLoginRepository {
    suspend fun signInWithGoogle(idToken: String): AuthResult
}
