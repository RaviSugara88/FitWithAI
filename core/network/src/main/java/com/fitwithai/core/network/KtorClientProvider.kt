package com.fitwithai.core.network

import io.ktor.client.HttpClient

interface KtorClientProvider {
    fun create(): HttpClient
}
