package com.fitwithai.data.remote

import com.fitwithai.core.datastore.TokenManager
import com.fitwithai.core.network.ApiConfig
import com.fitwithai.core.network.ApiRoutes
import com.fitwithai.core.network.AuthTokens
import com.fitwithai.core.network.TokenProvider
import com.fitwithai.core.network.createAuthClient
import com.fitwithai.data.remote.dto.AuthSessionDto
import com.fitwithai.data.remote.dto.RefreshRequestDto
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

/**
 * Bridges the Ktor `Auth` plugin to [TokenManager]. Reads the stored session for outgoing requests
 * and, on 401, mints a new one via `POST /v1/auth/refresh` using a bearer-free client (so refresh
 * itself never recurses). When refresh fails the session is cleared and null is returned, signalling
 * the caller to route back to login.
 */
class DefaultTokenProvider(
    private val tokenManager: TokenManager,
    private val config: ApiConfig,
) : TokenProvider {

    private val authClient by lazy { createAuthClient(config) }

    override suspend fun current(): AuthTokens? {
        val access = tokenManager.getToken() ?: return null
        val refresh = tokenManager.getRefreshToken() ?: return null
        return AuthTokens(access = access, refresh = refresh)
    }

    override suspend fun refresh(): AuthTokens? {
        val refreshToken = tokenManager.getRefreshToken() ?: return null
        val session = runCatching {
            authClient.post(ApiRoutes.AUTH_REFRESH) {
                setBody(RefreshRequestDto(refreshToken))
            }.body<AuthSessionDto>()
        }.getOrNull()

        if (session == null) {
            tokenManager.clearToken()
            return null
        }
        tokenManager.saveTokens(session.accessToken, session.refreshToken)
        return AuthTokens(access = session.accessToken, refresh = session.refreshToken)
    }
}
