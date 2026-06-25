package com.fitwithai.core.datastore

import com.russhwolf.settings.Settings

/** Non-secret user preferences (theme, etc.) over multiplatform-settings. */
class AppPreferences(private val settings: Settings) {

    var darkTheme: Boolean
        get() = settings.getBoolean(KEY_DARK_THEME, false)
        set(value) = settings.putBoolean(KEY_DARK_THEME, value)

    private companion object {
        const val KEY_DARK_THEME = "dark_theme"
    }
}
