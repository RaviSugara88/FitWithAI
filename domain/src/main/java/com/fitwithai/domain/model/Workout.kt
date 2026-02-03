package com.fitwithai.domain.model

data class Workout(
    val id: String,
    val type: String,
    val durationMinutes: Int,
    val caloriesBurned: Int,
)
