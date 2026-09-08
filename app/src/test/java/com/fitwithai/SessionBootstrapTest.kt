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
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/** `/auth/me` session bootstrap over Ktor's [MockEngine] (runBlocking — see [NetworkLayerTest]). */
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

    private fun sessionRepo(meEngine: MockEngine, tokenManager: TokenManager): SessionRepositoryImpl {
        val apiClient = createHttpClient(config, NoTokens, meEngine)
        val remote = AuthRemoteDataSource(
            config = config,
            apiClient = apiClient,
            engine = MockEngine { respond("{}", HttpStatusCode.OK, jsonHeaders) },
        )
        return SessionRepositoryImpl(remote, tokenManager)
    }

    @Test
    fun `no stored token returns null without calling the server`() = runBlocking {
        val tokenManager = TokenManager(InMemorySecureStorage())
        val engine = MockEngine { respond("{}", HttpStatusCode.OK, jsonHeaders) }

        assertNull(sessionRepo(engine, tokenManager).bootstrap())
    }

    @Test
    fun `valid session returns the current user`() = runBlocking {
        val tokenManager = TokenManager(InMemorySecureStorage()).apply { saveTokens("acc", "ref") }
        val engine = MockEngine { request ->
            if (request.url.encodedPath == ApiRoutes.AUTH_ME) {
                respond("""{"id":"u1","displayName":"Ravi"}""", HttpStatusCode.OK, jsonHeaders)
            } else {
                respond("{}", HttpStatusCode.OK, jsonHeaders)
            }
        }

        val user = sessionRepo(engine, tokenManager).bootstrap()

        assertEquals("u1", user?.id)
    }

    @Test
    fun `expired session clears tokens and returns null`() = runBlocking {
        val tokenManager = TokenManager(InMemorySecureStorage()).apply { saveTokens("acc", "ref") }
        val engine = MockEngine {
            respond("""{"error":{"code":"UNAUTHORIZED"}}""", HttpStatusCode.Unauthorized, jsonHeaders)
        }

        val user = sessionRepo(engine, tokenManager).bootstrap()

        assertNull(user)
        assertNull(tokenManager.getToken())
    }
}
