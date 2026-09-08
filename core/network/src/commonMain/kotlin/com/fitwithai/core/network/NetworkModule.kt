package com.fitwithai.core.network

import org.koin.dsl.module

/**
 * Shared network graph. Provides the authed application [io.ktor.client.HttpClient]; [ApiConfig] is
 * supplied by the platform module (varies per build) and [TokenProvider] by `:data`.
 */
val networkModule = module {
    single { createHttpClient(config = get(), tokenProvider = get()) }
}
