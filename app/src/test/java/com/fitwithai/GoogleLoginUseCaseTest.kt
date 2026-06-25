package com.fitwithai

import com.fitwithai.domain.model.AuthResult
import com.fitwithai.domain.repository.GoogleLoginRepository
import com.fitwithai.domain.usecase.GoogleLoginUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GoogleLoginUseCaseTest {

    @Test
    fun `returns success result from repository`() = runTest {
        val repo = object : GoogleLoginRepository {
            override suspend fun signInWithGoogle(idToken: String): AuthResult =
                AuthResult(isNewUser = true, token = "token-$idToken")
        }
        val useCase = GoogleLoginUseCase(repo)

        val result = useCase("abc")

        assertTrue(result.isSuccess)
        assertEquals("token-abc", result.getOrNull()?.token)
    }

    @Test
    fun `wraps repository failure in Result`() = runTest {
        val repo = object : GoogleLoginRepository {
            override suspend fun signInWithGoogle(idToken: String): AuthResult =
                error("network down")
        }
        val useCase = GoogleLoginUseCase(repo)

        val result = useCase("abc")

        assertTrue(result.isFailure)
        assertEquals("network down", result.exceptionOrNull()?.message)
    }
}
