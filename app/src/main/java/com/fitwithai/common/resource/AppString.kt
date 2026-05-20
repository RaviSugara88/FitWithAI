package com.fitwithai.common.resource

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class AppString {

    data class DynamicString(val value: String) : AppString()

    class ResourceString(
        @StringRes val resId: Int,
        vararg val args: Any,
    ) : AppString()

    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is ResourceString -> stringResource(resId, *args)
        }
    }

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is ResourceString -> context.getString(resId, *args)
        }
    }
}
