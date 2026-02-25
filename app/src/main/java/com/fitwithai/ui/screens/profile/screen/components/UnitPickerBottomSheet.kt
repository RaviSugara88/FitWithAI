package com.fitwithai.ui.screens.profile.screen.components
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fitwithai.ui.screens.profile.screen.components.UnitSegmentedControl
import com.fitwithai.ui.screens.profile.screen.components.WheelPicker

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
    val tealColor = Color(0xFF4DB6AC) // Matching the target UI accent color

    ModalBottomSheet(
        onDismissRequest = onCancel,
        sheetState = sheetState,
        containerColor = Color(0xFF1E1E1E), // Slightly darker background
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
                color = Color.LightGray
            )

            Spacer(Modifier.height(32.dp))

            // Centered Segmented Control
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                UnitSegmentedControl(
                    units = units,
                    selectedUnit = selectedUnit,
                    onUnitSelected = onUnitChange
                )
            }

            Spacer(Modifier.height(32.dp))

            // Wheel Picker now receives the selectedUnit
            WheelPicker(
                items = values,
                selectedItem = selectedValue,
                selectedUnit = selectedUnit,
                onItemSelected = onValueChange
            )

            Spacer(Modifier.height(32.dp))

            Divider(color = Color.DarkGray)

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onCancel) {
                    Text("Cancel", color = tealColor)
                }
                Spacer(Modifier.width(8.dp))
                TextButton(onClick = onConfirm) {
                    Text("OK", color = tealColor)
                }
            }
        }
    }
}