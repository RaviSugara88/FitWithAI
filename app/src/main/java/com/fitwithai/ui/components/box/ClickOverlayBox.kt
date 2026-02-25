package com.fitwithai.ui.components.box

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.semantics.Role

@Composable
fun ClickOverlayBox(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    showRipple: Boolean = false,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = if (showRipple) null else null,
                role = Role.Button,
                onClick = onClick
            )
    )
}