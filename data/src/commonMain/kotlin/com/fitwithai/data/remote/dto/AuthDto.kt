package com.fitwithai.data.remote.dto

import kotlinx.serialization.Serializable

/** Request body for `POST /v1/auth/refresh` (no bearer required). */
@Serializable
data class RefreshRequestDto(val refreshToken: String)

/** `POST /v1/auth/google` — the on-device Google ID token exchanged for a first-party session. */
@Serializable
data class GoogleLoginRequestDto(val idToken: String)

/** `POST /v1/auth/instagram` — the raw Instagram OAuth code + redirect URI. */
@Serializable
data class InstagramLoginRequestDto(val code: String, val redirectUri: String)

/** `POST /v1/auth/phone/request-otp`. */
@Serializable
data class RequestOtpRequestDto(val phoneE164: String)

@Serializable
data class RequestOtpResponseDto(val verificationId: String, val expiresInSec: Long = 0)

/** `POST /v1/auth/phone/verify-otp`. */
@Serializable
data class VerifyOtpRequestDto(val verificationId: String, val code: String)

/** `POST /v1/auth/logout` — revokes the refresh token. */
@Serializable
data class LogoutRequestDto(val refreshToken: String)

/** Session envelope returned by the auth endpoints (maps to domain `AuthResult` + token storage). */
@Serializable
data class AuthSessionDto(
    val accessToken: String,
    val refreshToken: String,
    val expiresInSec: Long = 0,
    val isNewUser: Boolean = false,
    val user: UserDto? = null,
)

@Serializable
data class UserDto(
    val id: String,
    val displayName: String? = null,
    val email: String? = null,
    val photoUrl: String? = null,
)
