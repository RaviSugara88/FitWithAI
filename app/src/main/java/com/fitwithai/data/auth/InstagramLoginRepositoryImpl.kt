package com.fitwithai.data.auth

import com.fitwithai.auth.ActivityProvider
import com.fitwithai.core.datastore.TokenManager
import com.fitwithai.domain.model.AuthResult
import com.fitwithai.domain.repository.InstagramLoginRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import kotlinx.coroutines.tasks.await

class InstagramLoginRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val tokenManager: TokenManager,
    private val activityProvider: ActivityProvider,
) : InstagramLoginRepository {

    override suspend fun signInWithInstagram(): AuthResult {
        val activity = activityProvider.current()
        val provider = OAuthProvider.newBuilder("instagram.com")
        val result = firebaseAuth
            .startActivityForSignInWithProvider(activity, provider.build())
            .await()

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
