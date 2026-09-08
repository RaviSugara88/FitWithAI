package com.fitwithai.core.datastore

/**
 * Stores the auth tokens in platform-encrypted storage. Shared across Android + iOS —
 * previously coupled to Android `Context` + DataStore + AndroidKeyStore.
 */
class TokenManager(private val secureStorage: SecureStorage) {

    suspend fun saveToken(token: String) = secureStorage.putString(TOKEN_KEY, token)

    suspend fun getToken(): String? = secureStorage.getString(TOKEN_KEY)

    /** Persists both the short-lived access token and the long-lived refresh token together. */
    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        secureStorage.putString(TOKEN_KEY, accessToken)
        secureStorage.putString(REFRESH_TOKEN_KEY, refreshToken)
    }

    suspend fun getRefreshToken(): String? = secureStorage.getString(REFRESH_TOKEN_KEY)

    suspend fun clearToken() {
        secureStorage.remove(TOKEN_KEY)
        secureStorage.remove(REFRESH_TOKEN_KEY)
    }

    private companion object {
        const val TOKEN_KEY = "auth_token"
        const val REFRESH_TOKEN_KEY = "refresh_token"
    }
}
