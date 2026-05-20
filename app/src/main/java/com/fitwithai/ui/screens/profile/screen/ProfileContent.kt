
package com.fitwithai.ui.screens.profile.screen

import com.fitwithai.common.resource.AppString
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fitwithai.R
import com.fitwithai.ui.components.textfield.CustomTextField
import com.fitwithai.ui.screens.profile.screen.components.BmiResultCard
import com.fitwithai.ui.screens.profile.screen.components.BottomActionBar
import com.fitwithai.ui.screens.profile.screen.components.DobInputField
import com.fitwithai.ui.screens.profile.screen.components.DobPickerBottomSheet
import com.fitwithai.ui.screens.profile.screen.components.ProfileTopBar
import com.fitwithai.ui.screens.profile.screen.components.SexDropdown
import com.fitwithai.ui.screens.profile.screen.components.StatelessScreenScaffold
import com.fitwithai.ui.screens.profile.screen.components.UnitPickerBottomSheet
import com.fitwithai.ui.screens.profile.state.PickerType
import com.fitwithai.ui.screens.profile.state.ProfileEvent
import com.fitwithai.ui.screens.profile.state.ProfileUiState
import java.time.LocalDate

@Composable
fun ProfileContent(
    uiState: ProfileUiState,
    onEvent: (ProfileEvent) -> Unit,
    ) {
    val interactionSource = remember { MutableInteractionSource() }
    var showDobSheet by remember { mutableStateOf(false) }
    var selectedDob by remember { mutableStateOf<LocalDate?>(null) }
    StatelessScreenScaffold(
        topBar = {
            ProfileTopBar(
                icon = Icons.Default.AccountCircle,
                contentDescription = "Profile Icon"
            )
        },

        bottomBar = {
            BottomActionBar(
                primaryText = AppString.ResourceString(R.string.next_button_text),
                isLoading = uiState.isLoading,
                isPrimaryEnabled = uiState.isSaveEnabled,
                onPrimaryClick = { onEvent(ProfileEvent.OnSaveClick) },
                onSecondaryClick = {
                   // onEvent(ProfileEvent.OnExitClick)
                }
            )
        }
    )
     { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(PaddingValues(start = 16.dp, end = 16.dp))
                .fillMaxSize()
                .background(Color.White),
            verticalArrangement = Arrangement.spacedBy( 10.dp)) {
            item {
                Text(
                    text = "Add Profile Info",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Black
                )
            }

            item {
                Text(
                    text = "Enter your details to calculate metrics like stride length and calories.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier
                        .weight(1f)

                    ) {
                        CustomTextField(
                            value = "${uiState.height} ${uiState.heightUnit}",
                            onValueChange = {},
                            label = "Height",
                            readOnly = true,
                            clickable = true,
                            enabled = true,
                           // inputType = InputType.Number,
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize() // Forces this Box to be the exact size of the CustomTextField
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null // Or use LocalIndication.current if you want a ripple effect!
                                ) {
                                    Log.d("ProfileDebug", "UI -> Height Clicked via Overlay!")
                                    onEvent(ProfileEvent.OnHeightClick)
                                }
                        )
                    }


                    Spacer(Modifier.width(16.dp))
                    Box(modifier = Modifier
                        .weight(1f)

                    ){
                        CustomTextField(
                            value =  "${uiState.weight} ${uiState.weightUnit}",
                            onValueChange = {},
                            label = "Weight",
                            readOnly = true,
                            clickable = true,
                            enabled = true,
                            // inputType = InputType.Number,
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize() // Forces this Box to be the exact size of the CustomTextField
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null // Or use LocalIndication.current if you want a ripple effect!
                                ) {
                                    Log.d("ProfileDebug", "UI -> Height Clicked via Overlay!")
                                    onEvent(ProfileEvent.OnWeightClick)
                                }
                        )
                    }



                }
            }

            item {
                Box(modifier = Modifier.fillMaxWidth()) {
                    SexDropdown(
                        selectedSex = uiState.selectedSex,
                        onSexSelected = {
                            Log.d("ProfileDebug", "UI -> SexDropdown")

                            onEvent(ProfileEvent.OnSexSelected(it))
                        }
                    )

                }

            }

            item {
                DobInputField(
                    selectedDate = selectedDob,
                    onClick = { showDobSheet = true }
                )

                if (showDobSheet) {
                    DobPickerBottomSheet(
                        onDismiss = { showDobSheet = false },
                        onDateSelected = { newDate ->
                            selectedDob = newDate
                            // Here you can calculate age:
                            // val age = Period.between(newDate, LocalDate.now()).years
                        }
                    )
                }
            }

            item {
                BmiResultCard(
                    weightKg = 90.0,
                    heightCm = 152.4, // roughly 5ft
                    sex = "Female",
                    age = 34
                )
            }

        }
    }
    uiState.activePicker?.let { picker ->
        Log.d("ProfileDebug", "activePicker -> : $")
        val isHeight = picker == PickerType.HEIGHT

        UnitPickerBottomSheet(
            title = if (isHeight) "Height" else "Weight",
            description = if (isHeight)
                "Used to estimate stride length."
            else
                "Used to estimate calorie burn.",
            units = if (isHeight)
                listOf("cm", "ft")
            else
                listOf("kg", "lb", "st"),
            selectedUnit = if (isHeight)
                uiState.heightUnit
            else
                uiState.weightUnit,
            values = if (isHeight)
                (100..220).toList()
            else
                (30..200).toList(),
            selectedValue = if (isHeight)
                uiState.height
            else
                uiState.weight,
            onUnitChange = {
                onEvent(
                    if (isHeight)
                        ProfileEvent.OnHeightUnitChange(it)
                    else
                        ProfileEvent.OnWeightUnitChange(it)
                )
            },
            onValueChange = {
                onEvent(
                    if (isHeight)
                        ProfileEvent.OnHeightChange(it)
                    else
                        ProfileEvent.OnWeightChange(it)
                )
            },
            onCancel = {
                onEvent(ProfileEvent.OnDismissPicker)
            },
            onConfirm = {
                onEvent(ProfileEvent.OnDismissPicker)
            }
        )
    }

}
