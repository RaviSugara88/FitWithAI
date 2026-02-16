package com.fitwithai.auth

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider

data class AuthOutcome(
    val isNewUser: Boolean,
    val token: String,
)

class AuthRepository(
    private val auth: FirebaseAuth,
    private val tokenStorage: TokenStorage,
) {
    fun signInWithGoogle(idToken: String, onResult: (Result<AuthOutcome>) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener { result ->
                val isNewUser = result.additionalUserInfo?.isNewUser == true
                auth.currentUser
                    ?.getIdToken(true)
                    ?.addOnSuccessListener { tokenResult ->
                        val token = tokenResult.token.orEmpty()
                        tokenStorage.saveToken(token)
                        onResult(Result.success(AuthOutcome(isNewUser, token)))
                    }
                    ?.addOnFailureListener { error ->
                        onResult(Result.failure(error))
                    }
            }
            .addOnFailureListener { error ->
                onResult(Result.failure(error))
            }
    }

    fun signInWithInstagram(activity: Activity, onResult: (Result<AuthOutcome>) -> Unit) {
        val provider = OAuthProvider.newBuilder("instagram.com")
        auth.startActivityForSignInWithProvider(activity, provider.build())
            .addOnSuccessListener { result ->
                val isNewUser = result.additionalUserInfo?.isNewUser == true
                auth.currentUser
                    ?.getIdToken(true)
                    ?.addOnSuccessListener { tokenResult ->
                        val token = tokenResult.token.orEmpty()
                        tokenStorage.saveToken(token)
                        onResult(Result.success(AuthOutcome(isNewUser, token)))
                    }
                    ?.addOnFailureListener { error ->
                        onResult(Result.failure(error))
                    }
            }
            .addOnFailureListener { error ->
                onResult(Result.failure(error))
            }
    }
}
