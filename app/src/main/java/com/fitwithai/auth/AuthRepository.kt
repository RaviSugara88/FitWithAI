package com.fitwithai.auth

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import kotlinx.coroutines.tasks.await

data class AuthOutcome(
    val isNewUser: Boolean,
    val token: String,
)

class AuthRepository(
    private val auth: FirebaseAuth,
    private val tokenStorage: TokenStorage,
) {

    suspend fun signInWithGoogle(idToken: String): AuthOutcome {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        val result = auth.signInWithCredential(credential).await()
        val isNewUser = result.additionalUserInfo?.isNewUser == true

        val tokenResult = auth.currentUser
            ?.getIdToken(true)
            ?.await()
            ?: error("User token retrieval failed")

        val token = tokenResult.token.orEmpty()

        tokenStorage.saveToken(token)

        return AuthOutcome(isNewUser, token)
    }

    suspend fun signInWithInstagram(activity: Activity): AuthOutcome {
        val provider = OAuthProvider.newBuilder("instagram.com")

        val result = auth
            .startActivityForSignInWithProvider(activity, provider.build())
            .await()

        val isNewUser = result.additionalUserInfo?.isNewUser == true

        val tokenResult = auth.currentUser
            ?.getIdToken(true)
            ?.await()
            ?: error("User token retrieval failed")

        val token = tokenResult.token.orEmpty()

        tokenStorage.saveToken(token)

        return AuthOutcome(isNewUser, token)
    }
}
