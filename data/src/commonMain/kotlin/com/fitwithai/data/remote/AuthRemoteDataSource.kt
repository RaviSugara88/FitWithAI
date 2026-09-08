package com.fitwithai.data.remote

import com.fitwithai.common.state.ResultState
import com.fitwithai.core.network.ApiConfig
import com.fitwithai.core.network.ApiRoutes
import com.fitwithai.core.network.createAuthClient
import com.fitwithai.core.network.safeApiCall
import com.fitwithai.data.remote.dto.AuthSessionDto
import com.fitwithai.data.remote.dto.GoogleLoginRequestDto
import com.fitwithai.data.remote.dto.InstagramLoginRequestDto
import com.fitwithai.data.remote.dto.LogoutRequestDto
import com.fitwithai.data.remote.dto.RequestOtpRequestDto
import com.fitwithai.data.remote.dto.RequestOtpResponseDto
import com.fitwithai.data.remote.dto.UserDto
import com.fitwithai.data.remote.dto.VerifyOtpRequestDto
import com.fitwithai.core.network.platformHttpEngine
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

/**
 * Backend auth call sites (§2.1). Login/logout hit the `/v1/auth` routes with a bearer-free client
 * (created from [config]) so the `Auth` plugin never attaches a token or tries to refresh during
 * sign-in; `/v1/auth/me` uses the authed [apiClient]. On-device OAuth acquisition (Google
 * Credential Manager, Instagram web flow, SMS) stays in the platform host — only the resulting
 * credential is sent up here.
 */
class AuthRemoteDataSource(
    config: ApiConfig,
    private val apiClient: HttpClient,
    engine: HttpClientEngine = platformHttpEngine(),
) {
    private val authClient: HttpClient by lazy { createAuthClient(config, engine) }

    suspend fun google(idToken: String): ResultState<AuthSessionDto> = safeApiCall {
        authClient.post(ApiRoutes.AUTH_GOOGLE) { setBody(GoogleLoginRequestDto(idToken)) }.body()
    }

    suspend fun instagram(code: String, redirectUri: String): ResultState<AuthSessionDto> = safeApiCall {
        authClient.post(ApiRoutes.AUTH_INSTAGRAM) {
            setBody(InstagramLoginRequestDto(code, redirectUri))
        }.body()
    }

    suspend fun requestOtp(phoneE164: String): ResultState<RequestOtpResponseDto> = safeApiCall {
        authClient.post(ApiRoutes.AUTH_REQUEST_OTP) { setBody(RequestOtpRequestDto(phoneE164)) }.body()
    }

    suspend fun verifyOtp(verificationId: String, code: String): ResultState<AuthSessionDto> = safeApiCall {
        authClient.post(ApiRoutes.AUTH_VERIFY_OTP) {
            setBody(VerifyOtpRequestDto(verificationId, code))
        }.body()
    }

    suspend fun logout(refreshToken: String): ResultState<Unit> = safeApiCall {
        authClient.post(ApiRoutes.AUTH_LOGOUT) { setBody(LogoutRequestDto(refreshToken)) }
        Unit
    }

    /** Current user for splash/session bootstrap — requires a valid bearer token. */
    suspend fun me(): ResultState<UserDto> = safeApiCall {
        apiClient.get(ApiRoutes.AUTH_ME).body()
    }
}
