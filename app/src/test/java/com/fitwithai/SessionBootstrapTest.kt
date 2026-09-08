package com.fitwithai

import com.fitwithai.core.datastore.SecureStorage
import com.fitwithai.core.datastore.TokenManager
import com.fitwithai.core.network.ApiConfig
import com.fitwithai.core.network.ApiRoutes
import com.fitwithai.core.network.AuthTokens
import com.fitwithai.core.network.TokenProvider
import com.fitwithai.core.network.createHttpClient
import com.fitwithai.data.auth.SessionRepositoryImpl
import com.fitwithai.data.remote.AuthRemoteDataSource
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandler
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/** `/auth/me` + `/auth/logout` session flows over Ktor's [MockEngine] (runBlocking — see [NetworkLayerTest]). */
class SessionBootstrapTest {

    private val config = ApiConfig(baseUrl = "http://localhost/", platformName = "test", appVersion = "test")
    private val jsonHeaders = headersOf(HttpHeaders.ContentType, "application/json")

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

    /**
     * [handler] drives both the authed `/me` client and the bearer-free `/logout` client, so a
     * single routing lambda covers whichever endpoint the test exercises.
     */
    private fun sessionRepo(tokenManager: TokenManager, handler: MockRequestHandler): SessionRepositoryImpl {
        val apiClient = createHttpClient(config, NoTokens, MockEngine(handler))
        val remote = AuthRemoteDataSource(config = config, apiClient = apiClient, engine = MockEngine(handler))
        return SessionRepositoryImpl(remote, tokenManager)
    }

    @Test
    fun `no stored token returns null without calling the server`() = runBlocking {
        val tokenManager = TokenManager(InMemorySecureStorage())

        assertNull(sessionRepo(tokenManager) { respond("{}", HttpStatusCode.OK, jsonHeaders) }.bootstrap())
    }

    @Test
    fun `valid session returns the current user`() = runBlocking {
        val tokenManager = TokenManager(InMemorySecureStorage()).apply { saveTokens("acc", "ref") }

        val user = sessionRepo(tokenManager) { request ->
            if (request.url.encodedPath == ApiRoutes.AUTH_ME) {
                respond("""{"id":"u1","displayName":"Ravi"}""", HttpStatusCode.OK, jsonHeaders)
            } else {
                respond("{}", HttpStatusCode.OK, jsonHeaders)
            }
        }.bootstrap()

        assertEquals("u1", user?.id)
    }

    @Test
    fun `expired session clears tokens and returns null`() = runBlocking {
        val tokenManager = TokenManager(InMemorySecureStorage()).apply { saveTokens("acc", "ref") }

        val user = sessionRepo(tokenManager) {
            respond("""{"error":{"code":"UNAUTHORIZED"}}""", HttpStatusCode.Unauthorized, jsonHeaders)
        }.bootstrap()

        assertNull(user)
        assertNull(tokenManager.getToken())
    }

    @Test
    fun `logout posts the refresh token and clears local tokens`() = runBlocking {
        var logoutBody: String? = null
        val tokenManager = TokenManager(InMemorySecureStorage()).apply { saveTokens("acc", "ref") }

        sessionRepo(tokenManager) { request ->
            if (request.url.encodedPath == ApiRoutes.AUTH_LOGOUT) {
                logoutBody = (request.body as? io.ktor.http.content.TextContent)?.text
                respond("", HttpStatusCode.NoContent)
            } else {
                respond("{}", HttpStatusCode.OK, jsonHeaders)
            }
        }.logout()

        assertEquals(true, logoutBody!!.contains("ref"))
        assertNull(tokenManager.getToken())
        assertNull(tokenManager.getRefreshToken())
    }

    @Test
    fun `logout clears tokens even when the server call fails`() = runBlocking {
        val tokenManager = TokenManager(InMemorySecureStorage()).apply { saveTokens("acc", "ref") }

        sessionRepo(tokenManager) { respond("boom", HttpStatusCode.InternalServerError) }.logout()

        assertNull(tokenManager.getToken())
        assertNull(tokenManager.getRefreshToken())
    }
}
