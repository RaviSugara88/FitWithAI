package com.fitwithai.data.workout

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.fitwithai.common.dispatcher.DispatcherProvider
import com.fitwithai.core.database.FitWithAiDatabase
import com.fitwithai.domain.model.Workout
import com.fitwithai.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import com.fitwithai.core.database.Workout as WorkoutRow

/**
 * Offline-first [WorkoutRepository] backed by the shared SQLDelight database. Reads stream
 * from disk as a [Flow]; writes go straight to local storage. (Remote sync is a later step.)
 */
class WorkoutRepositoryImpl(
    db: FitWithAiDatabase,
    private val dispatchers: DispatcherProvider,
) : WorkoutRepository {

    private val queries = db.workoutQueries

    override fun observeWorkouts(): Flow<List<Workout>> =
        queries.selectAll()
            .asFlow()
            .mapToList(dispatchers.io)
            .map { rows -> rows.map { it.toDomain() } }

    override suspend fun upsert(workout: Workout) {
        withContext(dispatchers.io) {
            queries.upsert(
                id = workout.id,
                name = workout.name,
                durationMinutes = workout.durationMinutes.toLong(),
                calories = workout.calories.toLong(),
                performedAtEpochMs = workout.performedAtEpochMs,
            )
        }
    }

    override suspend fun delete(id: String) {
        withContext(dispatchers.io) {
            queries.deleteById(id)
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
