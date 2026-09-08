package com.fitwithai.data.remote.mapper

import com.fitwithai.data.remote.dto.WorkoutDto
import com.fitwithai.domain.model.Workout

/** Wire → domain. Transport-only fields (`clientId`, `updatedAtEpochMs`, `deleted`) are dropped. */
fun WorkoutDto.toDomain(): Workout =
    Workout(
        id = id,
        name = name,
        durationMinutes = durationMinutes,
        calories = calories,
        performedAtEpochMs = performedAtEpochMs,
    )

/** Domain → wire. Sync metadata is supplied by the caller (repository), not the UI. */
fun Workout.toDto(
    clientId: String? = null,
    updatedAtEpochMs: Long = 0,
    deleted: Boolean = false,
): WorkoutDto =
    WorkoutDto(
        id = id,
        clientId = clientId,
        name = name,
        durationMinutes = durationMinutes,
        calories = calories,
        performedAtEpochMs = performedAtEpochMs,
        updatedAtEpochMs = updatedAtEpochMs,
        deleted = deleted,
    )
