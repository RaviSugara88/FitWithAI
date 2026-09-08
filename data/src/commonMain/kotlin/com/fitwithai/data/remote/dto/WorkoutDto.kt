package com.fitwithai.data.remote.dto

import kotlinx.serialization.Serializable

/**
 * Wire model for a workout (§4). A superset of domain `Workout`: the extra fields
 * (`clientId`, `updatedAtEpochMs`, `deleted`) drive idempotent create and delta sync and never
 * reach the UI.
 */
@Serializable
data class WorkoutDto(
    val id: String,
    val clientId: String? = null,
    val name: String,
    val durationMinutes: Int,
    val calories: Int,
    val performedAtEpochMs: Long,
    val updatedAtEpochMs: Long = 0,
    val deleted: Boolean = false,
)

/** Batch delta-sync request body for `POST /v1/workouts/sync`. */
@Serializable
data class SyncRequestDto(
    val changes: List<WorkoutDto>,
    val deletions: List<String>,
    val since: Long,
)

/** Batch delta-sync response body. */
@Serializable
data class SyncResultDto(
    val applied: Int = 0,
    val serverChanges: List<WorkoutDto> = emptyList(),
    val serverTime: Long = 0,
)
