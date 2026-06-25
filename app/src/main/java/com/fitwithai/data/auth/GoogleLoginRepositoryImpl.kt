package com.fitwithai.data.auth

import com.fitwithai.core.datastore.TokenManager
import com.fitwithai.domain.model.AuthResult
import com.fitwithai.domain.repository.GoogleLoginRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class GoogleLoginRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val tokenManager: TokenManager,
) : GoogleLoginRepository {

    override suspend fun signInWithGoogle(idToken: String): AuthResult {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val result = firebaseAuth.signInWithCredential(credential).await()
        val isNewUser = result.additionalUserInfo?.isNewUser == true

        val tokenResult = firebaseAuth.currentUser
            ?.getIdToken(true)
            ?.await()
            ?: error("User token retrieval failed")

        val token = tokenResult.token.orEmpty()
        tokenManager.saveToken(token)

        return AuthResult(isNewUser = isNewUser, token = token)
    }
}
