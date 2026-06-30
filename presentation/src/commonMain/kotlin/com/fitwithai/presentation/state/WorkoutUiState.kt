package com.fitwithai.presentation.state

import com.fitwithai.domain.model.Workout

sealed class WorkoutUiState {
    data object Loading : WorkoutUiState()
    data class Data(val workouts: List<Workout>) : WorkoutUiState()
    data class Error(val message: String) : WorkoutUiState()
}
