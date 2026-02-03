package com.fitwithai.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey val id: String,
    val type: String,
    val durationMinutes: Int,
    val caloriesBurned: Int,
)
