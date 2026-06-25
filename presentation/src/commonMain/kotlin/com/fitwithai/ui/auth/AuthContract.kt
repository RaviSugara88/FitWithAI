package com.fitwithai.ui.auth

import com.fitwithai.domain.model.AuthResult

/**
 * MVP contract for the imperative, platform-callback-driven OAuth flows (Google / Instagram).
 *
 * Compose screens are MVVM, but the credential/OAuth handshake needs an imperative,
 * platform-specific surface (Android Credential Manager / Firebase, iOS Google-SignIn pod).
 * The [View] is implemented per platform; the [Presenter] orchestrates the shared logic.
 */
interface AuthContract {

    /** Implemented by each platform host (Android Activity / iOS UIViewController). */
    interface View {
        /** Launches the platform Google credential UI and returns the resulting ID token. */
        suspend fun requestGoogleIdToken(): String

        fun showLoading(loading: Boolean)
        fun showError(message: String)
        fun onAuthenticated(result: AuthResult)
    }

    /** Shared presenter — drives the flow, depends only on domain use cases. */
    interface Presenter {
        fun attach(view: View)
        fun detach()
        fun onGoogleSignInClicked()
        fun onInstagramSignInClicked()
    }
}
