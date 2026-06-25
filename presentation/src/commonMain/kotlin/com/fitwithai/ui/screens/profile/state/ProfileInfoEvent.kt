package com.fitwithai.ui.screens.profile.state

sealed class ProfileEvent {

    data object OnHeightClick : ProfileEvent()
    data object OnWeightClick : ProfileEvent()
    data object OnDismissPicker : ProfileEvent()

    data class OnHeightChange(val value: Int) : ProfileEvent()
    data class OnWeightChange(val value: Int) : ProfileEvent()

    data class OnHeightUnitChange(val unit: String) : ProfileEvent()
    data class OnWeightUnitChange(val unit: String) : ProfileEvent()

    data class OnSexSelected(val sex: Sex) : ProfileEvent()

    data object OnSaveClick : ProfileEvent()
}
