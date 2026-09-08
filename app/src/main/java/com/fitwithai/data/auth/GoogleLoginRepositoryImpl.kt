package com.fitwithai.data.auth

import com.fitwithai.common.state.ResultState
import com.fitwithai.core.datastore.TokenManager
import com.fitwithai.data.remote.AuthRemoteDataSource
import com.fitwithai.domain.model.AuthResult
import com.fitwithai.domain.repository.GoogleLoginRepository

/**
 * Exchanges the on-device Google ID token (from Credential Manager) for a first-party session via
 * `POST /v1/auth/google`, persisting the access + refresh tokens so the Ktor `Auth` plugin can
 * attach and refresh them. OAuth acquisition stays on-device; only the ID token is sent up.
 */
class GoogleLoginRepositoryImpl(
    private val authRemote: AuthRemoteDataSource,
    private val tokenManager: TokenManager,
) : GoogleLoginRepository {

    override suspend fun signInWithGoogle(idToken: String): AuthResult {
        val session = when (val result = authRemote.google(idToken)) {
            is ResultState.Ok -> result.value
            is ResultState.Err -> throw result.throwable
        }
        tokenManager.saveTokens(accessToken = session.accessToken, refreshToken = session.refreshToken)
        return AuthResult(isNewUser = session.isNewUser, token = session.accessToken)
    }
}
