package com.fitwithai.domain.usecase

import com.fitwithai.domain.model.AuthResult
import com.fitwithai.domain.repository.PhoneAuthRepository

class VerifyOtpUseCase(
    private val repository: PhoneAuthRepository,
) {
    suspend operator fun invoke(verificationId: String, code: String): Result<AuthResult> =
        runCatching { repository.verifyOtp(verificationId, code) }
}
