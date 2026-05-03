package com.fitwithai.data.auth

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.fitwithai.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GoogleCredentialProviderImpl(
    private val context: Context,
) : GoogleCredentialProvider {

    override suspend fun getGoogleIdToken(activity: ComponentActivity): String {
        val serverClientId = context.getString(R.string.default_web_client_id)
        check(serverClientId.isNotBlank()) {
            "Add your Firebase Web OAuth client ID to default_web_client_id in strings.xml"
        }

        val credentialManager = CredentialManager.create(context)

        return withContext(Dispatchers.Main.immediate) {
            val buttonFlowRequest = GetCredentialRequest.Builder()
                .addCredentialOption(
                    GetSignInWithGoogleOption.Builder(serverClientId).build(),
                )
                .build()

            val response = try {
                credentialManager.getCredential(
                    context = activity,
                    request = buttonFlowRequest,
                )
            } catch (e: NoCredentialException) {
                // No authorized account yet — allow any Google account on the device (Android docs).
                val fallbackRequest = GetCredentialRequest.Builder()
                    .addCredentialOption(
                        GetGoogleIdOption.Builder()
                            .setFilterByAuthorizedAccounts(false)
                            .setServerClientId(serverClientId)
                            .build(),
                    )
                    .build()
                credentialManager.getCredential(
                    context = activity,
                    request = fallbackRequest,
                )
            } catch (e: GetCredentialException) {
                throw IllegalStateException(googleSignInHelpMessage(e), e)
            }

            extractIdToken(response)
        }
    }

    private fun extractIdToken(response: GetCredentialResponse): String {
        val credential = response.credential
        if (credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            val idToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
            check(idToken.isNotBlank()) {
                "Google returned an empty ID token. Confirm default_web_client_id matches your Firebase Web OAuth client."
            }
            return idToken
        }
        error("Unexpected credential type")
    }

    private fun googleSignInHelpMessage(e: GetCredentialException): String {
        val raw = buildString {
            append(e.message ?: e.toString())
            e.cause?.message?.let { append(" — ").append(it) }
        }
        if (!looksLikeReauthFailure(raw)) return raw

        return buildString {
            appendLine(raw.trim())
            appendLine()
            appendLine("This usually means Google/Firebase does not trust this app build yet. Fix:")
            appendLine("• Firebase Console → Project settings → Your apps → Android app → add SHA-1 for the keystore you use (debug: run ./gradlew signingReport).")
            appendLine("• Ensure package name is ${context.packageName} in Firebase.")
            appendLine("• default_web_client_id must be the Web OAuth client from the same Firebase/Google Cloud project.")
            appendLine("• If OAuth consent is in Testing, add your Google account under Test users (Google Cloud Console → OAuth consent screen).")
        }
    }

    private fun looksLikeReauthFailure(message: String): Boolean {
        val m = message.lowercase()
        return m.contains("reauth") || m.contains("[16]") ||
            (m.contains("16") && m.contains("account"))
    }
}
