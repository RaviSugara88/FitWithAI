import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class AppString {

    // 1. For hardcoded strings or text coming from an API
    data class DynamicString(val value: String) : AppString()

    // 2. For strings coming from your strings.xml (supports format arguments!)
    class ResourceString(
        @StringRes val resId: Int,
        vararg val args: Any
    ) : AppString()

    // 3. Resolves the string inside a Jetpack Compose function
    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is ResourceString -> stringResource(resId, *args)
        }
    }

    // 4. Resolves the string in standard Kotlin code (like a normal Activity/Fragment)
    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is ResourceString -> context.getString(resId, *args)
        }
    }
}