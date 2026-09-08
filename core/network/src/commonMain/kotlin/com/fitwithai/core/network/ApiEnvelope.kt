package com.fitwithai.core.network

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * Standard paginated list envelope (§1.1). `nextCursor` is null at the end of the collection.
 */
@Serializable
data class Page<T>(
    val data: List<T> = emptyList(),
    val nextCursor: String? = null,
)

/** Standard error envelope (§1.2): `error.code` is a stable SCREAMING_SNAKE_CASE string. */
@Serializable
data class ErrorEnvelope(val error: ErrorBody)

@Serializable
data class ErrorBody(
    val code: String? = null,
    val message: String? = null,
    val details: JsonObject? = null,
)
