package com.fitwithai.domain.usecase.workout

import com.fitwithai.domain.model.Workout
import com.fitwithai.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow

class ObserveWorkoutsUseCase(
    private val repository: WorkoutRepository,
) {
    operator fun invoke(): Flow<List<Workout>> = repository.observeWorkouts()
}

class UpsertWorkoutUseCase(
    private val repository: WorkoutRepository,
) {
    suspend operator fun invoke(workout: Workout) = repository.upsert(workout)
}
