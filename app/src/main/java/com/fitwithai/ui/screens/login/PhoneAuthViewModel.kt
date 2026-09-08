package com.fitwithai.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitwithai.domain.usecase.RequestOtpUseCase
import com.fitwithai.domain.usecase.VerifyOtpUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PhoneAuthUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val phoneDisplay: String = "",
    val verificationId: String? = null,
)

sealed interface PhoneAuthEvent {
    data object OtpRequested : PhoneAuthEvent
    data object LoginSuccess : PhoneAuthEvent
}

/**
 * Drives the two-step phone login. A single instance is shared across the phone-entry and OTP
 * screens (scoped to the phone-auth nav graph) so the [PhoneAuthUiState.verificationId] from
 * [requestOtp] is available to [verifyOtp].
 */
class PhoneAuthViewModel(
    private val requestOtpUseCase: RequestOtpUseCase,
    private val verifyOtpUseCase: VerifyOtpUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PhoneAuthUiState())
    val uiState: StateFlow<PhoneAuthUiState> = _uiState.asStateFlow()

    private val _events = Channel<PhoneAuthEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    /** [rawPhone] is the local 10-digit number; assembled into an E.164 (+91) number here. */
    fun requestOtp(rawPhone: String) {
        val digits = rawPhone.filter { it.isDigit() }
        if (digits.length != 10) {
            _uiState.update { it.copy(errorMessage = "Enter a valid 10-digit number") }
            return
        }
        val phoneE164 = "+91$digits"
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            requestOtpUseCase(phoneE164)
                .onSuccess { challenge ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            phoneDisplay = "+91 $digits",
                            verificationId = challenge.verificationId,
                        )
                    }
                    _events.send(PhoneAuthEvent.OtpRequested)
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "Failed to send OTP") }
                }
        }
    }

    fun verifyOtp(code: String) {
        val verificationId = _uiState.value.verificationId
        if (verificationId == null) {
            _uiState.update { it.copy(errorMessage = "Please request an OTP first") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            verifyOtpUseCase(verificationId, code)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
                    _events.send(PhoneAuthEvent.LoginSuccess)
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = e.message ?: "Invalid OTP") }
                }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
