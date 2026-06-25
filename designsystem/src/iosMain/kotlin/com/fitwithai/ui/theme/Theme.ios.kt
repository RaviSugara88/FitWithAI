package com.fitwithai.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

// iOS has no system dynamic color source; always fall back to the static schemes.
@Composable
actual fun dynamicColorScheme(darkTheme: Boolean): ColorScheme? = null
