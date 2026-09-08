package com.fitwithai.domain.usecase

import com.fitwithai.domain.repository.SessionRepository

class LogoutUseCase(
    private val repository: SessionRepository,
) {
    suspend operator fun invoke() = repository.logout()
}
