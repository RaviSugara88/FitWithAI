package com.fitwithai.core.network

import io.ktor.client.plugins.logging.LogLevel

/**
 * Per-build networking configuration, supplied by the platform Koin module so `baseUrl` and
 * `logLevel` can vary between debug and release (and between Android and iOS). Kept transport-free
 * of Ktor internals except [LogLevel] so call sites never hard-code any of this.
 */
data class ApiConfig(
    val baseUrl: String,
    val platformName: String,
    val appVersion: String,
    val requestTimeoutMs: Long = 30_000,
    val connectTimeoutMs: Long = 15_000,
    val socketTimeoutMs: Long = 30_000,
    val logLevel: LogLevel = LogLevel.NONE,
)
