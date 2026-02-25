package com.fitwithai.ui.screens.profile.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun UnitSegmentedControl(
    units: List<String>,
    selectedUnit: String,
    onUnitSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color.DarkGray)
    ) {

        units.forEach { unit ->

            val isSelected = unit == selectedUnit

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(if (isSelected) Color(0xFF2E7D6B) else Color.Transparent)
                    .clickable { onUnitSelected(unit) }
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text(
                    text = unit,
                    color = if (isSelected) Color.White else Color.Gray
                )
            }
        }
    }
}