package com.fitwithai.domain.repository

import com.fitwithai.domain.model.Workout
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    fun observeWorkouts(): Flow<List<Workout>>
    suspend fun upsert(workout: Workout)
    suspend fun delete(id: String)
}
