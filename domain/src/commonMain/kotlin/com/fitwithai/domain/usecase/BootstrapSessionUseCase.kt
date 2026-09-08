package com.fitwithai.domain.usecase

import com.fitwithai.domain.model.User
import com.fitwithai.domain.repository.SessionRepository

/** Splash/session bootstrap: null → route to login, non-null → route to the app. */
class BootstrapSessionUseCase(
    private val repository: SessionRepository,
) {
    suspend operator fun invoke(): User? =
        runCatching { repository.bootstrap() }.getOrNull()
}
