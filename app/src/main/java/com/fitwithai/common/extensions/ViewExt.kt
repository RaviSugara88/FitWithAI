package com.fitwithai.common.extensions

import android.view.View

fun View.toPx(dp: Int): Int = (dp * resources.displayMetrics.density).toInt()

