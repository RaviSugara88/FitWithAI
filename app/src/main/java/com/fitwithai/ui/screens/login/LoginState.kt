package com.fitwithai.ui.screens.login

data class LoginUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

sealed interface LoginEvent {
    data object NavigateToDashboard : LoginEvent
}
