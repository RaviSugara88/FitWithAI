package com.fitwithai.domain.model

/** Result of requesting a phone OTP: the id to quote back when verifying, plus its lifetime. */
data class OtpChallenge(
    val verificationId: String,
    val expiresInSec: Long,
)
