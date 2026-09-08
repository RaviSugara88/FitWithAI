package com.fitwithai.core.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/** Platform HTTP engine: OkHttp on Android, Darwin (NSURLSession) on iOS. */
expect fun platformHttpEngine(): HttpClientEngine

/** Shared JSON policy — tolerant of unknown/absent fields so DTO evolution doesn't break clients. */
val appJson: Json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    encodeDefaults = true
    explicitNulls = false
}

/**
 * The application HTTP client: base URL, JSON, timeouts, bearer auth with automatic 401 refresh,
 * and logging — all installed once so no call site repeats them. `expectSuccess = true` turns
 * non-2xx into exceptions that [safeApiCall] maps to [NetworkError].
 */
fun createHttpClient(
    config: ApiConfig,
    tokenProvider: TokenProvider,
    engine: HttpClientEngine = platformHttpEngine(),
): HttpClient = HttpClient(engine) {
    expectSuccess = true

    install(ContentNegotiation) { json(appJson) }

    install(HttpTimeout) {
        requestTimeoutMillis = config.requestTimeoutMs
        connectTimeoutMillis = config.connectTimeoutMs
        socketTimeoutMillis = config.socketTimeoutMs
    }

    install(DefaultRequest) {
        url(config.baseUrl)
        contentType(ContentType.Application.Json)
        header("X-Client-Platform", config.platformName)
        header("X-Client-Version", config.appVersion)
    }

    install(Auth) {
        bearer {
            loadTokens {
                tokenProvider.current()?.let { BearerTokens(it.access, it.refresh) }
            }
            refreshTokens {
                tokenProvider.refresh()?.let { BearerTokens(it.access, it.refresh) }
            }
            sendWithoutRequest { request ->
                // Attach the bearer proactively on every route except the auth routes (/v1/auth/*).
                val segments = request.url.encodedPathSegments.filter { it.isNotEmpty() }
                !(segments.size >= 2 && segments[0] == "v1" && segments[1] == "auth")
            }
        }
    }

    install(Logging) { level = config.logLevel }
}

/**
 * A minimal, bearer-free client used to hit the auth routes (login and token refresh). Keeping it
 * separate from the authed client prevents the `Auth` plugin from recursing on 401 during refresh.
 */
fun createAuthClient(
    config: ApiConfig,
    engine: HttpClientEngine = platformHttpEngine(),
): HttpClient = HttpClient(engine) {
    expectSuccess = true

    install(ContentNegotiation) { json(appJson) }

    install(HttpTimeout) {
        requestTimeoutMillis = config.requestTimeoutMs
        connectTimeoutMillis = config.connectTimeoutMs
        socketTimeoutMillis = config.socketTimeoutMs
    }

    install(DefaultRequest) {
        url(config.baseUrl)
        contentType(ContentType.Application.Json)
        header("X-Client-Platform", config.platformName)
        header("X-Client-Version", config.appVersion)
    }

    install(Logging) { level = config.logLevel }
}
