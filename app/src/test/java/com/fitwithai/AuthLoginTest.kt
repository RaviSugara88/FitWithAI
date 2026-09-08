package com.fitwithai

import com.fitwithai.common.state.ResultState
import com.fitwithai.core.datastore.SecureStorage
import com.fitwithai.core.datastore.TokenManager
import com.fitwithai.core.network.ApiConfig
import com.fitwithai.core.network.ApiRoutes
import com.fitwithai.core.network.AuthTokens
import com.fitwithai.core.network.TokenProvider
import com.fitwithai.core.network.createHttpClient
import com.fitwithai.data.auth.GoogleLoginRepositoryImpl
import com.fitwithai.data.remote.AuthRemoteDataSource
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.TextContent
import io.ktor.http.headersOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** Auth login call sites over Ktor's [MockEngine] (runBlocking — see [NetworkLayerTest]). */
class AuthLoginTest {

    private val config = ApiConfig(baseUrl = "http://localhost/", platformName = "test", appVersion = "test")
    private val jsonHeaders = headersOf(HttpHeaders.ContentType, "application/json")
    private val sessionJson =
        """{"accessToken":"acc","refreshToken":"ref","expiresInSec":3600,"isNewUser":true,"user":{"id":"u1"}}"""

    private object NoTokens : TokenProvider {
        override suspend fun current(): AuthTokens? = null
        override suspend fun refresh(): AuthTokens? = null
    }

    private class InMemorySecureStorage : SecureStorage {
        private val map = mutableMapOf<String, String>()
        override suspend fun putString(key: String, value: String) { map[key] = value }
        override suspend fun getString(key: String): String? = map[key]
        override suspend fun remove(key: String) { map.remove(key) }
    }

    /** Builds an [AuthRemoteDataSource] whose bearer-free auth client uses [authEngine]. */
    private fun authSource(authEngine: MockEngine): AuthRemoteDataSource {
        val apiClient = createHttpClient(config, NoTokens, MockEngine { respond("{}", HttpStatusCode.OK, jsonHeaders) })
        return AuthRemoteDataSource(config = config, apiClient = apiClient, engine = authEngine)
    }

    @Test
    fun `google login posts the id token and parses the session`() = runBlocking {
        var sentBody: String? = null
        val engine = MockEngine { request ->
            if (request.url.encodedPath == ApiRoutes.AUTH_GOOGLE) {
                sentBody = (request.body as? TextContent)?.text
                respond(sessionJson, HttpStatusCode.OK, jsonHeaders)
            } else {
                respond("{}", HttpStatusCode.OK, jsonHeaders)
            }
        }

        val result = authSource(engine).google("google-id-token-xyz")

        assertTrue("was: $result", result is ResultState.Ok)
        val session = (result as ResultState.Ok).value
        assertEquals("acc", session.accessToken)
        assertEquals("ref", session.refreshToken)
        assertTrue("body was: $sentBody", sentBody!!.contains("google-id-token-xyz"))
    }

    @Test
    fun `google repository stores access and refresh tokens`() = runBlocking {
        val engine = MockEngine { respond(sessionJson, HttpStatusCode.OK, jsonHeaders) }
        val tokenManager = TokenManager(InMemorySecureStorage())
        val repo = GoogleLoginRepositoryImpl(authSource(engine), tokenManager)

        val authResult = repo.signInWithGoogle("google-id-token-xyz")

        assertTrue(authResult.isNewUser)
        assertEquals("acc", authResult.token)
        assertEquals("acc", tokenManager.getToken())
        assertEquals("ref", tokenManager.getRefreshToken())
    }
}
