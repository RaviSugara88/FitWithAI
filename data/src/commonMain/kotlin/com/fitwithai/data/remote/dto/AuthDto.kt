package com.fitwithai.data.remote.dto

import kotlinx.serialization.Serializable

/** Request body for `POST /v1/auth/refresh` (no bearer required). */
@Serializable
data class RefreshRequestDto(val refreshToken: String)

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
