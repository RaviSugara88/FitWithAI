package com.fitwithai

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.fitwithai.common.dispatcher.DispatcherProvider
import com.fitwithai.core.database.FitWithAiDatabase
import com.fitwithai.data.workout.WorkoutRepositoryImpl
import com.fitwithai.domain.model.Workout
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class WorkoutRepositoryTest {

    private class TestDispatchers(d: CoroutineDispatcher) : DispatcherProvider {
        override val main = d
        override val default = d
        override val io = d
    }

    private fun inMemoryDb(): FitWithAiDatabase {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        FitWithAiDatabase.Schema.create(driver)
        return FitWithAiDatabase(driver)
    }

    @Test
    fun `upsert then observe returns the workout`() = runTest {
        val repo = WorkoutRepositoryImpl(inMemoryDb(), TestDispatchers(StandardTestDispatcher(testScheduler)))

        repo.upsert(
            Workout(
                id = "w1",
                name = "Morning Run",
                durationMinutes = 30,
                calories = 250,
                performedAtEpochMs = 1_000L,
            ),
        )

        val workouts = repo.observeWorkouts().first()

        assertEquals(1, workouts.size)
        assertEquals("Morning Run", workouts.first().name)
        assertEquals(250, workouts.first().calories)
    }

    @Test
    fun `observe orders by most recent first`() = runTest {
        val repo = WorkoutRepositoryImpl(inMemoryDb(), TestDispatchers(StandardTestDispatcher(testScheduler)))

        repo.upsert(Workout("old", "Older", 10, 100, performedAtEpochMs = 1_000L))
        repo.upsert(Workout("new", "Newer", 20, 200, performedAtEpochMs = 5_000L))

        val workouts = repo.observeWorkouts().first()

        assertEquals(listOf("new", "old"), workouts.map { it.id })
    }
}
