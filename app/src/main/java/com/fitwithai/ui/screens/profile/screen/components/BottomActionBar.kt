package com.fitwithai.ui.screens.profile.screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fitwithai.ui.components.button.ButtonWithProgress

@Composable
fun BottomActionBar(
    modifier: Modifier = Modifier,
    primaryText: String,
    isLoading: Boolean,
    isPrimaryEnabled: Boolean,
    onPrimaryClick: () -> Unit,
    onSecondaryClick: () -> Unit,
    secondaryText: String = "Exit"
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        TextButton(
            onClick = onSecondaryClick
        ) {
            Text(text = secondaryText)
        }

        ButtonWithProgress(
            text = primaryText,
            isLoading = isLoading,
            enabled = isPrimaryEnabled,
            onClick = onPrimaryClick
        )
    }
}