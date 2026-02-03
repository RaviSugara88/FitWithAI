package com.fitwithai.domain.usecase

import com.fitwithai.domain.repository.WorkoutRepository

class ObserveWorkoutsUseCase(
    private val repository: WorkoutRepository,
) {
    operator fun invoke() = repository.observeWorkouts()
}
