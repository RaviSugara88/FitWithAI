package com.fitwithai.core.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/** Platform HTTP engine: OkHttp on Android, Darwin (NSURLSession) on iOS. */
expect fun platformHttpEngine(): HttpClientEngine

private val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

/** Builds a shared, JSON-configured Ktor client usable from common business logic. */
fun createHttpClient(engine: HttpClientEngine = platformHttpEngine()): HttpClient =
    HttpClient(engine) {
        install(ContentNegotiation) {
            json(json)
        }
    }
