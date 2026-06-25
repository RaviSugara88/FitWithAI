package com.fitwithai.core.datastore

/**
 * Stores the auth token in platform-encrypted storage. Now shared across Android + iOS —
 * previously coupled to Android `Context` + DataStore + AndroidKeyStore.
 */
class TokenManager(private val secureStorage: SecureStorage) {

    suspend fun saveToken(token: String) = secureStorage.putString(TOKEN_KEY, token)

    suspend fun getToken(): String? = secureStorage.getString(TOKEN_KEY)

    suspend fun clearToken() = secureStorage.remove(TOKEN_KEY)

    private companion object {
        const val TOKEN_KEY = "auth_token"
    }
}
