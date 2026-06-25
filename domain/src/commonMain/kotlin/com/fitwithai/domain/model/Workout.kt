package com.fitwithai.domain.model

data class Workout(
    val id: String,
    val name: String,
    val durationMinutes: Int,
    val calories: Int,
    val performedAtEpochMs: Long,
)
