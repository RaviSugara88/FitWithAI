package com.fitwithai.ui.screens.profile.screen.components
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.fitwithai.ui.screens.profile.state.Sex

@Composable
fun SexDropdown(
    selectedSex: Sex?,
    onSexSelected: (Sex) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    // 1. Everything lives inside this main Box now
    Box(modifier = modifier.fillMaxWidth()) {

        OutlinedTextField(
            value = selectedSex?.displayName ?: "",
            onValueChange = {},
            enabled = true,
            readOnly = true,
            label = { Text("Sex") },
            trailingIcon = {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Arrow",
                    tint = Color.Black
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Gray,
                unfocusedBorderColor = Color.Gray,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        // 2. The transparent overlay to catch the click
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable { expanded = true }
        )

        // 3. Wrap the DropdownMenu in a Box aligned to the BottomEnd
        Box(
            modifier = Modifier
                .matchParentSize()
                .wrapContentSize(Alignment.BottomEnd) // This forces the menu to the right!
        ) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                Sex.entries.forEach { sex ->
                    DropdownMenuItem(
                        text = { Text(sex.displayName) },
                        onClick = {
                            expanded = false
                            onSexSelected(sex)
                        }
                    )
                }
            }
        }
    }
}