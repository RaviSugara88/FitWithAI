package com.fitwithai.ui.screens.profile.screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BottomActionSection(
    isSaveEnabled: Boolean,
    isLoading: Boolean,
    onSaveClick: () -> Unit,
    onExitClick: () -> Unit
) {

    Column {

        TextButton(
            onClick = onExitClick
        ) {
            Text("Exit", color = Color.Cyan)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onSaveClick,
            enabled = isSaveEnabled && !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp),
                    color = Color.White
                )
            } else {
                Text("Save & Continue")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}