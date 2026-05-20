package com.fitwithai.domain.model

data class AuthResult(
    val isNewUser: Boolean,
    val token: String,
)
