package com.fitwithai

import com.fitwithai.common.state.ResultState
import com.fitwithai.core.network.ApiConfig
import com.fitwithai.core.network.ApiRoutes
import com.fitwithai.core.network.AuthTokens
import com.fitwithai.core.network.NetworkError
import com.fitwithai.core.network.TokenProvider
import com.fitwithai.core.network.createHttpClient
import com.fitwithai.data.remote.WorkoutRemoteDataSource
import io.ktor.client.request.post
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Network-layer tests over Ktor's [MockEngine]. Run on a real clock (`runBlocking`, not `runTest`)
 * because the `HttpTimeout` plugin's `delay()` fires spuriously under the virtual test clock.
 */
class NetworkLayerTest {

    private val config = ApiConfig(baseUrl = "http://localhost/", platformName = "test", appVersion = "test")
    private val jsonHeaders = headersOf(HttpHeaders.ContentType, "application/json")
    private val emptyPage = """{"data":[],"nextCursor":null}"""

    private object BearerTokens : TokenProvider {
        override suspend fun current() = AuthTokens(access = "access-123", refresh = "refresh-123")
        override suspend fun refresh(): AuthTokens? = null
    }

    @Test
    fun `bearer token is attached to non-auth routes and absent on auth routes`() = runBlocking {
        var workoutsAuth: String? = "unset"
        var authRouteAuth: String? = "unset"

        val engine = MockEngine { request ->
            when (request.url.encodedPath) {
                ApiRoutes.WORKOUTS -> {
                    workoutsAuth = request.headers[HttpHeaders.Authorization]
                    respond(emptyPage, HttpStatusCode.OK, jsonHeaders)
                }
                ApiRoutes.AUTH_REFRESH -> {
                    authRouteAuth = request.headers[HttpHeaders.Authorization]
                    respond("""{"accessToken":"a","refreshToken":"r"}""", HttpStatusCode.OK, jsonHeaders)
                }
                else -> respond("{}", HttpStatusCode.OK, jsonHeaders)
            }
        }
        val client = createHttpClient(config, BearerTokens, engine)

        WorkoutRemoteDataSource(client).fetch()
        client.post(ApiRoutes.AUTH_REFRESH)

        assertEquals("Bearer access-123", workoutsAuth)
        assertNull(authRouteAuth)
    }

    @Test
    fun `404 maps to NotFound with the server error code`() = runBlocking {
        val engine = MockEngine {
            respond(
                """{"error":{"code":"WORKOUT_NOT_FOUND","message":"nope"}}""",
                HttpStatusCode.NotFound,
                jsonHeaders,
            )
        }
        val result = WorkoutRemoteDataSource(createHttpClient(config, BearerTokens, engine)).fetch()

        assertTrue("was: $result", result is ResultState.Err)
        val error = (result as ResultState.Err).throwable
        assertTrue("was: $error", error is NetworkError.NotFound)
        assertEquals("WORKOUT_NOT_FOUND", (error as NetworkError.NotFound).code)
    }

    @Test
    fun `malformed json maps to a serialization error`() = runBlocking {
        val engine = MockEngine { respond("this is not json", HttpStatusCode.OK, jsonHeaders) }
        val result = WorkoutRemoteDataSource(createHttpClient(config, BearerTokens, engine)).fetch()

        assertTrue(result is ResultState.Err)
        assertTrue((result as ResultState.Err).throwable is NetworkError.Serialization)
    }
}
