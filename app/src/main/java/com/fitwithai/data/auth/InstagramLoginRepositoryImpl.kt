package com.fitwithai.data.auth

import android.app.Activity
import com.fitwithai.auth.TokenStorage
import com.fitwithai.domain.model.AuthResult
import com.fitwithai.domain.repository.InstagramLoginRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import kotlinx.coroutines.tasks.await

class InstagramLoginRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val tokenStorage: TokenStorage,
) : InstagramLoginRepository {

    override suspend fun signInWithInstagram(activity: Activity): AuthResult {
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
        tokenStorage.saveToken(token)

        return AuthResult(isNewUser = isNewUser, token = token)
    }
}
