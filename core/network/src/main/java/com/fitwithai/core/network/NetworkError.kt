package com.fitwithai.core.network

sealed class NetworkError(message: String, cause: Throwable? = null) : Throwable(message, cause) {
    class Timeout(cause: Throwable? = null) : NetworkError("Request timed out", cause)
    class Unreachable(cause: Throwable? = null) : NetworkError("Server unreachable", cause)
    class Unknown(cause: Throwable? = null) : NetworkError("Unknown network error", cause)
}
