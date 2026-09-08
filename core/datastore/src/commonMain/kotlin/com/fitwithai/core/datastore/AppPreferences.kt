package com.fitwithai.core.datastore

import com.russhwolf.settings.Settings

/** Non-secret user preferences (theme, etc.) over multiplatform-settings. */
class AppPreferences(private val settings: Settings) {

    var darkTheme: Boolean
        get() = settings.getBoolean(KEY_DARK_THEME, false)
        set(value) = settings.putBoolean(KEY_DARK_THEME, value)

    /** High-water mark (epoch ms) of the last successful workout delta sync. 0 = never synced. */
    var lastWorkoutSyncMs: Long
        get() = settings.getLong(KEY_LAST_WORKOUT_SYNC, 0L)
        set(value) = settings.putLong(KEY_LAST_WORKOUT_SYNC, value)

    private companion object {
        const val KEY_DARK_THEME = "dark_theme"
        const val KEY_LAST_WORKOUT_SYNC = "last_workout_sync_ms"
    }
}
