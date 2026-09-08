package com.fitwithai.data.auth

import com.fitwithai.common.state.ResultState
import com.fitwithai.core.datastore.TokenManager
import com.fitwithai.data.remote.AuthRemoteDataSource
import com.fitwithai.domain.model.AuthResult
import com.fitwithai.domain.model.OtpChallenge
import com.fitwithai.domain.repository.PhoneAuthRepository

/**
 * Phone login over the backend OTP endpoints. A successful verify persists the access + refresh
 * tokens (like the Google flow) so the session is usable immediately.
 */
class PhoneAuthRepositoryImpl(
    private val authRemote: AuthRemoteDataSource,
    private val tokenManager: TokenManager,
) : PhoneAuthRepository {

    override suspend fun requestOtp(phoneE164: String): OtpChallenge {
        val response = when (val result = authRemote.requestOtp(phoneE164)) {
            is ResultState.Ok -> result.value
            is ResultState.Err -> throw result.throwable
        }
        return OtpChallenge(response.verificationId, response.expiresInSec)
    }

    override suspend fun verifyOtp(verificationId: String, code: String): AuthResult {
        val session = when (val result = authRemote.verifyOtp(verificationId, code)) {
            is ResultState.Ok -> result.value
            is ResultState.Err -> throw result.throwable
        }
        tokenManager.saveTokens(accessToken = session.accessToken, refreshToken = session.refreshToken)
        return AuthResult(isNewUser = session.isNewUser, token = session.accessToken)
    }
}
