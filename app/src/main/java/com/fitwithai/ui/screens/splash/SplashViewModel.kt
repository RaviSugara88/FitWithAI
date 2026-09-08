package com.fitwithai.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitwithai.domain.usecase.BootstrapSessionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface SessionState {
    data object Loading : SessionState
    data object Authenticated : SessionState
    data object Unauthenticated : SessionState
}

/** Runs the `/auth/me` session bootstrap once on start and exposes where to route next. */
class SplashViewModel(
    private val bootstrapSessionUseCase: BootstrapSessionUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<SessionState>(SessionState.Loading)
    val state: StateFlow<SessionState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val user = bootstrapSessionUseCase()
            _state.value = if (user != null) SessionState.Authenticated else SessionState.Unauthenticated
        }
    }
}
