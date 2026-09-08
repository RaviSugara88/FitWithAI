package com.fitwithai.core.network

/** First-party session tokens the [io.ktor.client.plugins.auth.Auth] plugin attaches to requests. */
data class AuthTokens(val access: String, val refresh: String)

/**
 * Bridges the Ktor `Auth` plugin to token persistence without `:core:network` depending on
 * `:core:datastore` internals. Implemented in `:data` over `TokenManager` + `/v1/auth/refresh`.
 */
interface TokenProvider {
    /** Current stored tokens, or null when the user has no session. */
    suspend fun current(): AuthTokens?

    /**
     * Mints a fresh access token from the stored refresh token (called by the plugin on 401).
     * Returns null when refresh fails → caller must clear the session and re-authenticate.
     */
    suspend fun refresh(): AuthTokens?
}
