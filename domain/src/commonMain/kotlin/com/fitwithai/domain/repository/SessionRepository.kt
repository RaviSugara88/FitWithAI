package com.fitwithai.domain.repository

import com.fitwithai.domain.model.User

/**
 * Resolves the stored session on app start. Returns the current [User] when a valid session
 * exists, or null when the user must sign in (clearing any dead tokens as a side effect).
 */
interface SessionRepository {
    suspend fun bootstrap(): User?

    /** Revokes the session server-side (best effort) and always clears local tokens. */
    suspend fun logout()
}
