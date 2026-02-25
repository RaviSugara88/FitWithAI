package com.fitwithai.ui.screens.profile.screen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun <T> WheelPicker(
    items: List<T>,
    selectedItem: T,
    onItemSelected: (T) -> Unit
) {

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = items.indexOf(selectedItem)
    )

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { index ->
                if (index in items.indices) {
                    onItemSelected(items[index])
                }
            }
    }

    Box(
        modifier = Modifier
            .height(150.dp)
            .fillMaxWidth()
    ) {

        LazyColumn(
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemsIndexed(items) { index, item ->

                val isSelected =
                    index == listState.firstVisibleItemIndex

                Text(
                    text = item.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isSelected) Color.Cyan else Color.Gray,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }

        Divider(
            modifier = Modifier.align(Alignment.Center),
            thickness = 1.dp,
            color = Color.Cyan
        )
    }
}