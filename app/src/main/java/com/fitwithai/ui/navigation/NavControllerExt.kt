package com.fitwithai.ui.navigation

import androidx.navigation.NavController

fun NavController.safePop(): Boolean = popBackStack()

