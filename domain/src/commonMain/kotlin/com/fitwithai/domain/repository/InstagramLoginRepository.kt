package com.fitwithai.domain.repository

import com.fitwithai.domain.model.AuthResult

interface InstagramLoginRepository {
    /**
     * Runs the Instagram OAuth flow and returns the result. The platform-specific surface
     * (e.g. the foreground Activity on Android) is supplied to the implementation out of
     * band, so this stays free of platform types.
     */
    suspend fun signInWithInstagram(): AuthResult
}
