package com.fitwithai.domain.usecase

import android.app.Activity
import com.fitwithai.domain.model.AuthResult
import com.fitwithai.domain.repository.InstagramLoginRepository

class InstagramLoginUseCase(
    private val repository: InstagramLoginRepository,
) {
    suspend operator fun invoke(activity: Activity): Result<AuthResult> =
        runCatching { repository.signInWithInstagram(activity) }
}
