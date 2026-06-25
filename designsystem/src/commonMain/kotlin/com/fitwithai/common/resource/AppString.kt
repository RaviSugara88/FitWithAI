package com.fitwithai.common.resource

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * Platform-agnostic text holder. Lets ViewModels expose either a literal string or a
 * (multiplatform) string resource without depending on Android's `Context`/`R`.
 */
sealed class AppString {

    data class DynamicString(val value: String) : AppString()

    data class ResourceString(
        val resource: StringResource,
        val args: List<Any> = emptyList(),
    ) : AppString()

    @Composable
    fun asString(): String = when (this) {
        is DynamicString -> value
        is ResourceString ->
            if (args.isEmpty()) stringResource(resource)
            else stringResource(resource, *args.toTypedArray())
    }
}
