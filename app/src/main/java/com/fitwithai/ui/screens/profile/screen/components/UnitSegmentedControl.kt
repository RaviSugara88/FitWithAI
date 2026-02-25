package com.fitwithai.ui.screens.profile.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
            .border(1.dp, Color.DarkGray, RoundedCornerShape(50))
            .clip(RoundedCornerShape(50)), // Pill shape
        verticalAlignment = Alignment.CenterVertically
    ) {
        units.forEachIndexed { index, unit ->
            val isSelected = unit == selectedUnit

            Row(
                modifier = Modifier
                    .background(if (isSelected) Color(0xFF2A4B40) else Color.Transparent)
                    .clickable { onUnitSelected(unit) }
                    .padding(horizontal = 24.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier
                            .size(16.dp)
                            .padding(end = 4.dp)
                    )
                }
                Text(
                    text = unit,
                    color = if (isSelected) Color.White else Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Divider between items
            if (index < units.size - 1) {
                Divider(
                    color = Color.DarkGray,
                    modifier = Modifier
                        .height(24.dp)
                        .width(1.dp)
                )
            }
        }
    }
}