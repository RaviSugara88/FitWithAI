package com.fitwithai.data.local

import com.fitwithai.core.database.WorkoutDao
import com.fitwithai.core.database.WorkoutEntity
import kotlinx.coroutines.flow.Flow

class WorkoutLocalDataSource(
    private val workoutDao: WorkoutDao,
) {
    fun observeWorkouts(): Flow<List<WorkoutEntity>> = workoutDao.observeWorkouts()

    suspend fun upsertAll(items: List<WorkoutEntity>) = workoutDao.upsertAll(items)
}
