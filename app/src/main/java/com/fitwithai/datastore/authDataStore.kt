package com.fitwithai.datastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.authDataStore by preferencesDataStore(name = "auth_secure_store")
