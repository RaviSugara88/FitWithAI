package com.fitwithai.ui.screens.profile.screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun <T> UnitPickerBottomSheet(
    title: String,
    description: String,
    units: List<String>,
    selectedUnit: String,
    values: List<T>,
    selectedValue: T,
    onUnitChange: (String) -> Unit,
    onValueChange: (T) -> Unit,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onCancel,
        sheetState = sheetState,
        containerColor = Color(0xFF1C1C1C),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(Modifier.height(20.dp))

            UnitSegmentedControl(
                units = units,
                selectedUnit = selectedUnit,
                onUnitSelected = onUnitChange
            )

            Spacer(Modifier.height(24.dp))

            WheelPicker(
                items = values,
                selectedItem = selectedValue,
                onItemSelected = onValueChange
            )

            Spacer(Modifier.height(24.dp))

            Divider(color = Color.DarkGray)

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {

                TextButton(onClick = onCancel) {
                    Text("Cancel", color = Color.Cyan)
                }

                Spacer(Modifier.width(12.dp))

                TextButton(onClick = onConfirm) {
                    Text("OK", color = Color.Cyan)
                }
            }
        }
    }
}