package com.fitwithai.ui.screens.profile.screen.components

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
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
    selectedUnit: String,
    onItemSelected: (T) -> Unit
) {
    val itemHeight = 48.dp
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = maxOf(0, items.indexOf(selectedItem))
    )
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

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
            .height(itemHeight * 3) // Constrain height to show exactly 3 items
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior, // Makes the scroll snap to the item
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = itemHeight), // Pads top/bottom so first/last items can reach the center
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(items) { _, item ->
                val isSelected = item == selectedItem
                Box(
                    modifier = Modifier.height(itemHeight),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$item $selectedUnit", // Combines value and unit (e.g., "172 cm")
                        style = MaterialTheme.typography.titleMedium,
                        color = if (isSelected) Color(0xFF4DB6AC) else Color.Gray
                    )
                }
            }
        }

        // The two center highlight lines
        Column(
            modifier = Modifier.matchParentSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            VerticalDivider(color = Color.DarkGray, modifier = Modifier.width(120.dp))
            Spacer(modifier = Modifier.height(itemHeight))
            VerticalDivider(color = Color.DarkGray, modifier = Modifier.width(120.dp))
        }
    }
}