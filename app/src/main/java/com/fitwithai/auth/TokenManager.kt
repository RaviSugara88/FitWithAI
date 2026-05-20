package com.fitwithai.auth

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.fitwithai.security.EncryptionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

private val Context.tokenPreferences by preferencesDataStore(name = "auth_secure_store")

class TokenManager(private val context: Context) {

    private val tokenKey = stringPreferencesKey("auth_token")

    suspend fun saveToken(token: String) {
        val encrypted = EncryptionManager.encrypt(token)
        context.tokenPreferences.edit { preferences ->
            preferences[tokenKey] = encrypted
        }
    }

    val tokenFlow: Flow<String?> =
        context.tokenPreferences.data.map { preferences ->
            preferences[tokenKey]?.let { EncryptionManager.decrypt(it) }
        }

    suspend fun getToken(): String? = tokenFlow.firstOrNull()

    suspend fun clearToken() {
        context.tokenPreferences.edit { preferences ->
            preferences.remove(tokenKey)
        }
    }
}
