package com.fitwithai.datastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

/**
 * Auth-related preferences. Token persistence now lives in the shared
 * `com.fitwithai.core.datastore.TokenManager` (backed by `SecureStorage`).
 */
val Context.userPreferencesDataStore by preferencesDataStore(name = "user_preferences")
