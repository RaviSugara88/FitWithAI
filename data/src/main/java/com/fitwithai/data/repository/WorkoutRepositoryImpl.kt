package com.fitwithai.data.repository

import com.fitwithai.data.local.WorkoutLocalDataSource
import com.fitwithai.data.remote.WorkoutRemoteDataSource
import com.fitwithai.data.sync.WorkoutSyncManager
import com.fitwithai.domain.model.Workout
import com.fitwithai.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WorkoutRepositoryImpl(
    private val local: WorkoutLocalDataSource,
    private val remote: WorkoutRemoteDataSource,
    private val syncManager: WorkoutSyncManager,
) : WorkoutRepository {
    override fun observeWorkouts(): Flow<List<Workout>> {
        return local.observeWorkouts().map { entities ->
            entities.map { entity ->
                Workout(
                    id = entity.id,
                    type = entity.type,
                    durationMinutes = entity.durationMinutes,
                    caloriesBurned = entity.caloriesBurned,
                )
            }
        }
    }

    override suspend fun syncWorkouts() {
        syncManager.sync(remote, local)
    }
}
