package com.fitwithai.ui.screens.profile.state

import androidx.compose.runtime.Immutable

@Immutable
data class ProfileUiState(
    val height: Int = 170,
    val weight: Int = 65,
    val heightUnit: String = "cm",
    val weightUnit: String = "kg",
    val selectedSex: Sex? = null,
    val activePicker: PickerType? = null,
    val isSaveEnabled: Boolean = false,
    val isLoading: Boolean = false
)

enum class Sex(val displayName: String) {
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other")
}

enum class PickerType {
    HEIGHT,
    WEIGHT
}