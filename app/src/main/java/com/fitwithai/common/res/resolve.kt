package com.fitwithai.common.res

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource

@Composable
fun AppString.resolve(): String {
    return when (this) {
        is AppString.Resource -> stringResource(resId, *args.toTypedArray())
        is AppString.Plural -> pluralStringResource(resId, quantity, *args.toTypedArray())
        is AppString.Dynamic -> value
    }
}