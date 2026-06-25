package com.fitwithai.domain.usecase

import com.fitwithai.domain.model.AuthResult
import com.fitwithai.domain.repository.GoogleLoginRepository

class GoogleLoginUseCase(
    private val repository: GoogleLoginRepository,
) {
    suspend operator fun invoke(idToken: String): Result<AuthResult> =
        runCatching { repository.signInWithGoogle(idToken) }
}
