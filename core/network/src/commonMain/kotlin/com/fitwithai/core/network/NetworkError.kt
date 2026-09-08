package com.fitwithai.core.network

/**
 * Closed taxonomy every failed API call funnels into (§5). Extends [Exception] so it slots into
 * `ResultState.Err(throwable)`. The `code`/`fields` are read from the [ErrorEnvelope] body where
 * present; `NoConnectivity`/`Timeout`/`Serialization` come from transport-level failures.
 */
sealed class NetworkError(message: String? = null, cause: Throwable? = null) : Exception(message, cause) {
    data object NoConnectivity : NetworkError("No network connectivity") {
        private fun readResolve(): Any = NoConnectivity
    }

    data object Timeout : NetworkError("Request timed out") {
        private fun readResolve(): Any = Timeout
    }

    data class Unauthorized(val code: String?) : NetworkError("Unauthorized")            // 401 after refresh failed
    data class Forbidden(val code: String?) : NetworkError("Forbidden")                  // 403
    data class NotFound(val code: String?) : NetworkError("Not found")                   // 404
    data class Conflict(val code: String?) : NetworkError("Conflict")                    // 409
    data class Validation(val code: String?, val fields: Map<String, String>) : NetworkError("Validation failed") // 422
    data class RateLimited(val retryAfterSec: Long?) : NetworkError("Rate limited")      // 429
    data class Server(val status: Int, val code: String?) : NetworkError("Server error $status") // 5xx
    data class Serialization(val detail: String?) : NetworkError("Malformed response")
    data class Unknown(val detail: String?) : NetworkError("Unknown network error")
}
