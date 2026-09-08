package com.fitwithai.core.network

/**
 * Centralised, versioned path constants. Paths are relative — the base URL is applied once by
 * `DefaultRequest` in [createHttpClient], so nothing here (or at any call site) hard-codes a host.
 */
object ApiRoutes {

    // Auth
    const val AUTH_GOOGLE = "/v1/auth/google"
    const val AUTH_INSTAGRAM = "/v1/auth/instagram"
    const val AUTH_REQUEST_OTP = "/v1/auth/phone/request-otp"
    const val AUTH_VERIFY_OTP = "/v1/auth/phone/verify-otp"
    const val AUTH_REFRESH = "/v1/auth/refresh"
    const val AUTH_LOGOUT = "/v1/auth/logout"
    const val AUTH_ME = "/v1/auth/me"

    // Workouts
    const val WORKOUTS = "/v1/workouts"
    const val WORKOUTS_SYNC = "/v1/workouts/sync"
    const val WORKOUTS_CATALOG = "/v1/workouts/catalog"

    /** Prefix that the `Auth` plugin must NOT decorate with a bearer token (login/refresh). */
    const val AUTH_PREFIX = "/v1/auth/"
}
