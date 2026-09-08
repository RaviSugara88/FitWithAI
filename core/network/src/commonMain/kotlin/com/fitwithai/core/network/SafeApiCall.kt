package com.fitwithai.core.network

import com.fitwithai.common.state.ResultState
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.ContentConvertException
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

/**
 * Runs a Ktor call and funnels every failure into the [NetworkError] taxonomy, wrapped in
 * [ResultState]. `createHttpClient` sets `expectSuccess = true`, so non-2xx surfaces as
 * [ClientRequestException] (4xx) / [ServerResponseException] (5xx). [CancellationException] is
 * rethrown so coroutine cancellation is never swallowed.
 */
suspend inline fun <T> safeApiCall(crossinline block: suspend () -> T): ResultState<T> =
    try {
        ResultState.Ok(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: ClientRequestException) {
        ResultState.Err(e.toNetworkError())
    } catch (e: ServerResponseException) {
        ResultState.Err(NetworkError.Server(e.response.status.value, e.errorCodeOrNull()))
    } catch (e: HttpRequestTimeoutException) {
        ResultState.Err(NetworkError.Timeout)
    } catch (e: ContentConvertException) {
        ResultState.Err(NetworkError.Serialization(e.message))
    } catch (e: SerializationException) {
        ResultState.Err(NetworkError.Serialization(e.message))
    } catch (e: Throwable) {
        ResultState.Err(e.toConnectivityOrUnknown())
    }

/** Maps a 4xx [ClientRequestException] to the taxonomy, reading `code`/`fields` from the body. */
suspend fun ClientRequestException.toNetworkError(): NetworkError {
    val envelope = response.readErrorEnvelope()
    val code = envelope?.error?.code
    return when (response.status.value) {
        401 -> NetworkError.Unauthorized(code)
        403 -> NetworkError.Forbidden(code)
        404 -> NetworkError.NotFound(code)
        409 -> NetworkError.Conflict(code)
        422 -> NetworkError.Validation(code, envelope?.error?.details.toFieldMap())
        429 -> NetworkError.RateLimited(response.headers["Retry-After"]?.toLongOrNull())
        else -> NetworkError.Unknown(code ?: message)
    }
}

suspend fun ServerResponseException.errorCodeOrNull(): String? =
    response.readErrorEnvelope()?.error?.code

/** Reads the standard error envelope from a (possibly already-validated) response body. */
suspend fun HttpResponse.readErrorEnvelope(): ErrorEnvelope? =
    runCatching { appJson.decodeFromString(ErrorEnvelope.serializer(), bodyAsText()) }.getOrNull()

fun JsonObject?.toFieldMap(): Map<String, String> =
    this?.mapValues { (_, v) -> (v as? JsonPrimitive)?.content ?: v.toString() } ?: emptyMap()

/** Best-effort classification of transport-level throwables (kept KMP-common-safe). */
fun Throwable.toConnectivityOrUnknown(): NetworkError {
    val name = this::class.simpleName.orEmpty()
    val connectivity = name.contains("IOException") ||
        name.contains("UnresolvedAddress") ||
        name.contains("SocketException") ||
        name.contains("ConnectException") ||
        name.contains("UnknownHost")
    return if (connectivity) NetworkError.NoConnectivity else NetworkError.Unknown(message)
}
