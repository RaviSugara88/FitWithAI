package com.fitwithai.domain.usecase

import com.fitwithai.domain.model.OtpChallenge
import com.fitwithai.domain.repository.PhoneAuthRepository

class RequestOtpUseCase(
    private val repository: PhoneAuthRepository,
) {
    suspend operator fun invoke(phoneE164: String): Result<OtpChallenge> =
        runCatching { repository.requestOtp(phoneE164) }
}
