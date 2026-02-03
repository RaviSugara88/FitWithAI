package com.fitwithai.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitwithai.domain.usecase.ObserveWorkoutsUseCase
import com.fitwithai.presentation.state.WorkoutUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class WorkoutViewModel(
    observeWorkoutsUseCase: ObserveWorkoutsUseCase,
) : ViewModel() {
    val uiState: StateFlow<WorkoutUiState> = observeWorkoutsUseCase()
        .map { workouts -> WorkoutUiState.Data(workouts) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = WorkoutUiState.Loading,
        )
}
