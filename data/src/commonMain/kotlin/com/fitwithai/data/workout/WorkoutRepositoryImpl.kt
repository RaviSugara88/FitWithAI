package com.fitwithai.data.workout

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.fitwithai.common.dispatcher.DispatcherProvider
import com.fitwithai.common.state.ResultState
import com.fitwithai.common.time.currentEpochMillis
import com.fitwithai.core.database.FitWithAiDatabase
import com.fitwithai.core.datastore.AppPreferences
import com.fitwithai.data.remote.WorkoutRemoteDataSource
import com.fitwithai.data.remote.dto.WorkoutDto
import com.fitwithai.data.remote.mapper.toDto
import com.fitwithai.domain.model.Workout
import com.fitwithai.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import com.fitwithai.core.database.Workout as WorkoutRow

/**
 * Offline-first [WorkoutRepository]. SQLDelight is the single source of truth the UI observes;
 * writes are applied locally first (optimistic, flagged `pendingSync`) then pushed. [syncWorkouts]
 * reconciles with the backend via delta pull + batch push, tracking a high-water mark in
 * [AppPreferences].
 */
class WorkoutRepositoryImpl(
    db: FitWithAiDatabase,
    private val dispatchers: DispatcherProvider,
    private val remote: WorkoutRemoteDataSource,
    private val prefs: AppPreferences,
) : WorkoutRepository {

    private val queries = db.workoutQueries

    override fun observeWorkouts(): Flow<List<Workout>> =
        queries.selectAll()
            .asFlow()
            .mapToList(dispatchers.io)
            .map { rows -> rows.map { it.toDomain() } }

    override suspend fun upsert(workout: Workout) {
        withContext(dispatchers.io) {
            val now = currentEpochMillis()
            queries.upsert(
                id = workout.id,
                name = workout.name,
                durationMinutes = workout.durationMinutes.toLong(),
                calories = workout.calories.toLong(),
                performedAtEpochMs = workout.performedAtEpochMs,
                clientId = workout.id,
                updatedAtEpochMs = now,
                deleted = 0L,
                pendingSync = 1L,
            )
            // Best-effort immediate push; if it fails the row stays pending for syncWorkouts().
            val pushed = remote.upsert(workout.toDto(clientId = workout.id, updatedAtEpochMs = now))
            if (pushed is ResultState.Ok) {
                queries.clearPending(workout.id)
            }
        }
    }

    override suspend fun delete(id: String) {
        withContext(dispatchers.io) {
            // Soft-delete (tombstone) so the deletion propagates; hard-delete on server confirmation.
            queries.markDeleted(updatedAtEpochMs = currentEpochMillis(), id = id)
            if (remote.delete(id) is ResultState.Ok) {
                queries.hardDeleteById(id)
            }
        }
    }

    override suspend fun syncWorkouts() {
        withContext(dispatchers.io) {
            val since = prefs.lastWorkoutSyncMs
            var highWater = since

            // 1. Pull the server delta and merge it into local storage.
            when (val fetched = remote.fetch(updatedSince = since.takeIf { it > 0 })) {
                is ResultState.Ok -> fetched.value.data.forEach { dto ->
                    applyRemote(dto)
                    if (dto.updatedAtEpochMs > highWater) highWater = dto.updatedAtEpochMs
                }
                is ResultState.Err -> Unit // leave local state; retry on next sync
            }

            // 2. Push pending local changes/tombstones in one batch.
            val pending = queries.selectPendingChanges().executeAsList()
            if (pending.isNotEmpty()) {
                val changes = pending.filter { it.deleted == 0L }.map { it.toDto() }
                val deletions = pending.filter { it.deleted != 0L }.map { it.id }
                when (val result = remote.sync(changes, deletions, since)) {
                    is ResultState.Ok -> {
                        pending.forEach { row ->
                            if (row.deleted != 0L) queries.hardDeleteById(row.id)
                            else queries.clearPending(row.id)
                        }
                        result.value.serverChanges.forEach { applyRemote(it) }
                        if (result.value.serverTime > highWater) highWater = result.value.serverTime
                    }
                    is ResultState.Err -> Unit
                }
            }

            // 3. Advance the high-water mark only when we made progress.
            if (highWater > since) prefs.lastWorkoutSyncMs = highWater
        }
    }

    /** Applies a server-authoritative DTO: hard-delete on tombstone, otherwise a synced upsert. */
    private fun applyRemote(dto: WorkoutDto) {
        if (dto.deleted) {
            queries.hardDeleteById(dto.id)
        } else {
            queries.upsert(
                id = dto.id,
                name = dto.name,
                durationMinutes = dto.durationMinutes.toLong(),
                calories = dto.calories.toLong(),
                performedAtEpochMs = dto.performedAtEpochMs,
                clientId = dto.clientId,
                updatedAtEpochMs = dto.updatedAtEpochMs,
                deleted = 0L,
                pendingSync = 0L,
            )
        }
    }
}

private fun WorkoutRow.toDomain(): Workout =
    Workout(
        id = id,
        name = name,
        durationMinutes = durationMinutes.toInt(),
        calories = calories.toInt(),
        performedAtEpochMs = performedAtEpochMs,
    )

private fun WorkoutRow.toDto(): WorkoutDto =
    WorkoutDto(
        id = id,
        clientId = clientId,
        name = name,
        durationMinutes = durationMinutes.toInt(),
        calories = calories.toInt(),
        performedAtEpochMs = performedAtEpochMs,
        updatedAtEpochMs = updatedAtEpochMs,
        deleted = deleted != 0L,
    )
