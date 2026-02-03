package com.fitwithai.data.sync

import com.fitwithai.core.database.WorkoutEntity
import com.fitwithai.data.local.WorkoutLocalDataSource
import com.fitwithai.data.remote.WorkoutRemoteDataSource

class WorkoutSyncManager {
    suspend fun sync(
        remote: WorkoutRemoteDataSource,
        local: WorkoutLocalDataSource,
    ) {
        val workouts = remote.fetchWorkouts()
        val entities = workouts.map { workout ->
            WorkoutEntity(
                id = workout.id,
                type = workout.type,
                durationMinutes = workout.durationMinutes,
                caloriesBurned = workout.caloriesBurned,
            )
        }
        local.upsertAll(entities)
    }
}
