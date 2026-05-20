package com.fitwithai.common.extensions

import android.content.Context

fun Context?.safePackageName(): String? = this?.packageName

