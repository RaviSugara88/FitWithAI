package com.fitwithai.auth

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.fitwithai.datastore.authDataStore
import com.fitwithai.security.SecureCryptoManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class TokenStorage(private val context: Context) {

    private val TOKEN_KEY = stringPreferencesKey("auth_token")

    suspend fun saveToken(token: String) {
        val encrypted = SecureCryptoManager.encrypt(token)

        context.authDataStore.edit { preferences ->
            preferences[TOKEN_KEY] = encrypted
        }
    }

    val tokenFlow: Flow<String?> =
        context.authDataStore.data.map { preferences ->
            preferences[TOKEN_KEY]?.let {
                SecureCryptoManager.decrypt(it)
            }
        }

    suspend fun getToken(): String? {
        return tokenFlow.firstOrNull()
    }

    suspend fun clearToken() {
        context.authDataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }
}
