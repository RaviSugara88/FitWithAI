package com.fitwithai.data.auth

import com.fitwithai.common.state.ResultState
import com.fitwithai.core.datastore.TokenManager
import com.fitwithai.core.network.NetworkError
import com.fitwithai.data.remote.AuthRemoteDataSource
import com.fitwithai.domain.model.User
import com.fitwithai.domain.repository.SessionRepository

/**
 * Validates the stored session against `GET /v1/auth/me`. A dead session (401 after the Auth
 * plugin's refresh failed) clears the tokens and forces login; a transient failure (offline /
 * server) keeps the user signed in optimistically so the offline-first UI still opens.
 */
class SessionRepositoryImpl(
    private val authRemote: AuthRemoteDataSource,
    private val tokenManager: TokenManager,
) : SessionRepository {

    override suspend fun bootstrap(): User? {
        tokenManager.getToken() ?: return null
        return when (val result = authRemote.me()) {
            is ResultState.Ok -> User(id = result.value.id)
            is ResultState.Err -> {
                if (result.throwable is NetworkError.Unauthorized) {
                    tokenManager.clearToken()
                    null
                } else {
                    // Transient (no connectivity / server error) — stay signed in.
                    User()
                }
            }
        }
    }
}
