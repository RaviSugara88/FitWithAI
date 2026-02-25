package com.fitwithai.ui.screens.profile.viewmodel

import AppString
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitwithai.R
import com.fitwithai.ui.screens.profile.state.PickerType
import com.fitwithai.ui.screens.profile.state.ProfileEvent
import com.fitwithai.ui.screens.profile.state.ProfileUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    //test
    // Default state uses a local string resource (e.g., R.string.loading_text)
    private val _titleText = MutableStateFlow<AppString>(
        AppString.ResourceString(R.string.welcome_user)
    )
    val titleText: StateFlow<AppString> = _titleText.asStateFlow()
    fun onEvent(event: ProfileEvent) {
        when (event) {

            ProfileEvent.OnHeightClick -> {
                Log.d("ProfileDebug", "ViewMode -> Height: $")
                _uiState.update { it.copy(activePicker = PickerType.HEIGHT) }
            }

            ProfileEvent.OnWeightClick -> {
                Log.d("ProfileDebug", "ViewMode -> Weight: $")

                _uiState.update { it.copy(activePicker = PickerType.WEIGHT) }
            }

            ProfileEvent.OnDismissPicker -> {
                _uiState.update { it.copy(activePicker = null) }
            }

            is ProfileEvent.OnHeightChange -> {
                _uiState.update { current ->
                    val updated = current.copy(height = event.value)
                    updated.copy(isSaveEnabled = validate(updated))
                }
            }

            is ProfileEvent.OnWeightChange -> {
                _uiState.update { current ->
                    val updated = current.copy(weight = event.value)
                    updated.copy(isSaveEnabled = validate(updated))
                }
            }

            is ProfileEvent.OnHeightUnitChange -> {
                _uiState.update { it.copy(heightUnit = event.unit) }
            }

            is ProfileEvent.OnWeightUnitChange -> {
                _uiState.update { it.copy(weightUnit = event.unit) }
            }

            is ProfileEvent.OnSexSelected -> {
                _uiState.update { current ->
                    val updated = current.copy(selectedSex = event.sex)
                    updated.copy(isSaveEnabled = validate(updated))
                }
            }

            ProfileEvent.OnSaveClick -> saveProfile()
        }
    }

    private fun validate(state: ProfileUiState) =
        state.selectedSex != null &&
                state.height > 0 &&
                state.weight > 0

    private fun saveProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(1000)
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}