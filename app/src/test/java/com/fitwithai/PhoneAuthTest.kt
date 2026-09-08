package com.fitwithai

import com.fitwithai.core.datastore.SecureStorage
import com.fitwithai.core.datastore.TokenManager
import com.fitwithai.core.network.ApiConfig
import com.fitwithai.core.network.ApiRoutes
import com.fitwithai.core.network.AuthTokens
import com.fitwithai.core.network.TokenProvider
import com.fitwithai.core.network.createHttpClient
import com.fitwithai.data.auth.PhoneAuthRepositoryImpl
import com.fitwithai.data.remote.AuthRemoteDataSource
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

/** Phone/OTP login flow over Ktor's [MockEngine] (runBlocking — see [NetworkLayerTest]). */
class PhoneAuthTest {

    private val config = ApiConfig(baseUrl = "http://localhost/", platformName = "test", appVersion = "test")
    private val jsonHeaders = headersOf(HttpHeaders.ContentType, "application/json")
    private val sessionJson =
        """{"accessToken":"acc","refreshToken":"ref","isNewUser":false,"user":{"id":"u1"}}"""

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

    private fun repo(engine: MockEngine, tokenManager: TokenManager): PhoneAuthRepositoryImpl {
        val apiClient = createHttpClient(config, NoTokens, MockEngine { respond("{}", HttpStatusCode.OK, jsonHeaders) })
        val remote = AuthRemoteDataSource(config = config, apiClient = apiClient, engine = engine)
        return PhoneAuthRepositoryImpl(remote, tokenManager)
    }

    @Test
    fun `request otp returns the challenge`() = runBlocking {
        val engine = MockEngine { request ->
            if (request.url.encodedPath == ApiRoutes.AUTH_REQUEST_OTP) {
                respond("""{"verificationId":"vrf_1","expiresInSec":300}""", HttpStatusCode.OK, jsonHeaders)
            } else {
                respond("{}", HttpStatusCode.OK, jsonHeaders)
            }
        }

        val challenge = repo(engine, TokenManager(InMemorySecureStorage())).requestOtp("+919876543210")

        assertEquals("vrf_1", challenge.verificationId)
        assertEquals(300L, challenge.expiresInSec)
    }

    @Test
    fun `verify otp stores tokens and returns the session`() = runBlocking {
        var verifyBody: String? = null
        val engine = MockEngine { request ->
            if (request.url.encodedPath == ApiRoutes.AUTH_VERIFY_OTP) {
                verifyBody = (request.body as? io.ktor.http.content.TextContent)?.text
                respond(sessionJson, HttpStatusCode.OK, jsonHeaders)
            } else {
                respond("{}", HttpStatusCode.OK, jsonHeaders)
            }
        }
        val tokenManager = TokenManager(InMemorySecureStorage())

        val result = repo(engine, tokenManager).verifyOtp("vrf_1", "000000")

        assertEquals("acc", result.token)
        assertEquals("acc", tokenManager.getToken())
        assertEquals("ref", tokenManager.getRefreshToken())
        assertEquals(true, verifyBody!!.contains("vrf_1"))
        assertEquals(true, verifyBody!!.contains("000000"))
    }
}
