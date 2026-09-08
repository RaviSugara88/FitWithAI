package com.fitwithai.domain.repository

import com.fitwithai.domain.model.Workout
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    fun observeWorkouts(): Flow<List<Workout>>
    suspend fun upsert(workout: Workout)
    suspend fun delete(id: String)

    /**
     * Reconciles local SQLDelight state with the backend: pulls the server delta since the last
     * sync high-water mark and pushes any pending local changes/tombstones. Safe to call
     * repeatedly (idempotent); the UI keeps observing [observeWorkouts] throughout.
     */
    suspend fun syncWorkouts()
}
