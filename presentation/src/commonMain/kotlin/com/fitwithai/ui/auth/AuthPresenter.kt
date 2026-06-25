package com.fitwithai.ui.auth

import com.fitwithai.domain.usecase.GoogleLoginUseCase
import com.fitwithai.domain.usecase.InstagramLoginUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * Shared MVP presenter for the OAuth flows. Holds no platform types — it asks the
 * [AuthContract.View] to perform the platform credential handshake, then runs the
 * resulting token through pure domain use cases.
 */
class AuthPresenter(
    private val googleLoginUseCase: GoogleLoginUseCase,
    private val instagramLoginUseCase: InstagramLoginUseCase,
) : AuthContract.Presenter {

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var view: AuthContract.View? = null

    override fun attach(view: AuthContract.View) {
        this.view = view
    }

    override fun detach() {
        view = null
        scope.cancel()
    }

    override fun onGoogleSignInClicked() {
        val view = view ?: return
        scope.launch {
            view.showLoading(true)
            runCatching { view.requestGoogleIdToken() }
                .mapCatching { idToken -> googleLoginUseCase(idToken).getOrThrow() }
                .onSuccess { result ->
                    view.showLoading(false)
                    view.onAuthenticated(result)
                }
                .onFailure { error ->
                    view.showLoading(false)
                    view.showError(error.message ?: "Google sign-in failed")
                }
        }
    }

    override fun onInstagramSignInClicked() {
        val view = view ?: return
        scope.launch {
            view.showLoading(true)
            instagramLoginUseCase()
                .onSuccess { result ->
                    view.showLoading(false)
                    view.onAuthenticated(result)
                }
                .onFailure { error ->
                    view.showLoading(false)
                    view.showError(error.message ?: "Instagram sign-in failed")
                }
        }
    }
}
