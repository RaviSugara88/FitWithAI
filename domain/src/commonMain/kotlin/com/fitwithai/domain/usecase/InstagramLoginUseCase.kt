package com.fitwithai.domain.usecase

import com.fitwithai.domain.model.AuthResult
import com.fitwithai.domain.repository.InstagramLoginRepository

class InstagramLoginUseCase(
    private val repository: InstagramLoginRepository,
) {
    suspend operator fun invoke(): Result<AuthResult> =
        runCatching { repository.signInWithInstagram() }
}
