package com.fitwithai

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.fitwithai.common.dispatcher.DispatcherProvider
import com.fitwithai.core.database.FitWithAiDatabase
import com.fitwithai.core.datastore.AppPreferences
import com.fitwithai.core.network.ApiConfig
import com.fitwithai.core.network.AuthTokens
import com.fitwithai.core.network.TokenProvider
import com.fitwithai.core.network.createHttpClient
import com.fitwithai.data.remote.WorkoutRemoteDataSource
import com.fitwithai.data.workout.WorkoutRepositoryImpl
import com.fitwithai.domain.model.Workout
import com.russhwolf.settings.MapSettings
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
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

    private object NoTokens : TokenProvider {
        override suspend fun current(): AuthTokens? = null
        override suspend fun refresh(): AuthTokens? = null
    }

    private val testConfig = ApiConfig(baseUrl = "http://localhost/", platformName = "test", appVersion = "test")
    private val jsonHeaders = headersOf(HttpHeaders.ContentType, "application/json")

    private fun inMemoryDb(): FitWithAiDatabase {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        FitWithAiDatabase.Schema.create(driver)
        return FitWithAiDatabase(driver)
    }

    /** Remote source over a MockEngine that echoes writes and serves the given fetch page. */
    private fun remote(fetchPageJson: String = """{"data":[],"nextCursor":null}"""): WorkoutRemoteDataSource {
        val engine = MockEngine { request ->
            val path = request.url.encodedPath
            when {
                path == "/v1/workouts" && request.method == HttpMethod.Get ->
                    respond(fetchPageJson, HttpStatusCode.OK, jsonHeaders)
                path == "/v1/workouts" && request.method == HttpMethod.Post ->
                    respond(
                        """{"id":"srv","name":"srv","durationMinutes":1,"calories":1,"performedAtEpochMs":1}""",
                        HttpStatusCode.OK, jsonHeaders,
                    )
                path == "/v1/workouts/sync" ->
                    respond("""{"applied":0,"serverChanges":[],"serverTime":0}""", HttpStatusCode.OK, jsonHeaders)
                path.startsWith("/v1/workouts/") && request.method == HttpMethod.Delete ->
                    respond("", HttpStatusCode.NoContent)
                else -> respond("{}", HttpStatusCode.OK, jsonHeaders)
            }
        }
        return WorkoutRemoteDataSource(createHttpClient(testConfig, NoTokens, engine))
    }

    private fun repo(
        db: FitWithAiDatabase = inMemoryDb(),
        remote: WorkoutRemoteDataSource = remote(),
        scheduler: kotlinx.coroutines.test.TestCoroutineScheduler,
    ) = WorkoutRepositoryImpl(
        db = db,
        dispatchers = TestDispatchers(StandardTestDispatcher(scheduler)),
        remote = remote,
        prefs = AppPreferences(MapSettings()),
    )

    @Test
    fun `upsert then observe returns the workout`() = runTest {
        val repo = repo(scheduler = testScheduler)

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
        val repo = repo(scheduler = testScheduler)

        repo.upsert(Workout("old", "Older", 10, 100, performedAtEpochMs = 1_000L))
        repo.upsert(Workout("new", "Newer", 20, 200, performedAtEpochMs = 5_000L))

        val workouts = repo.observeWorkouts().first()

        assertEquals(listOf("new", "old"), workouts.map { it.id })
    }

    @Test
    fun `sync pulls server delta into the database and advances the high-water mark`() = runTest {
        val prefs = AppPreferences(MapSettings())
        val db = inMemoryDb()
        val serverPage = """
            {"data":[{"id":"srv1","name":"Server Push","durationMinutes":45,"calories":380,
            "performedAtEpochMs":1725148800000,"updatedAtEpochMs":5000,"deleted":false}],"nextCursor":null}
        """.trimIndent()
        val repo = WorkoutRepositoryImpl(
            db = db,
            dispatchers = TestDispatchers(StandardTestDispatcher(testScheduler)),
            remote = remote(fetchPageJson = serverPage),
            prefs = prefs,
        )

        repo.syncWorkouts()

        val workouts = repo.observeWorkouts().first()
        assertEquals(listOf("srv1"), workouts.map { it.id })
        assertEquals(5000L, prefs.lastWorkoutSyncMs)
    }
}
