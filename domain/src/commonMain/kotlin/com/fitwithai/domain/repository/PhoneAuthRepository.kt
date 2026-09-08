package com.fitwithai.domain.repository

import com.fitwithai.domain.model.AuthResult
import com.fitwithai.domain.model.OtpChallenge

/**
 * Phone/SMS login. OTP delivery + verification happen server-side; a successful [verifyOtp]
 * yields a first-party session (tokens are persisted by the implementation).
 */
interface PhoneAuthRepository {
    suspend fun requestOtp(phoneE164: String): OtpChallenge
    suspend fun verifyOtp(verificationId: String, code: String): AuthResult
}
