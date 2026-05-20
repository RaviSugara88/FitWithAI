package com.fitwithai.datastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

/**
 * Auth-related preferences. Token persistence is implemented in [com.fitwithai.auth.TokenManager]
 * using a private DataStore delegate to avoid duplicate `authDataStore` extensions on case-insensitive filesystems.
 */
val Context.userPreferencesDataStore by preferencesDataStore(name = "user_preferences")
