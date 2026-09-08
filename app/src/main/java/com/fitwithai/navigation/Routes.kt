package com.fitwithai.navigation

object Routes {
    const val LOGIN = "login"
    const val DASHBOARD = "dashboard"

    // Phone/OTP login: a nested graph so the two screens share one PhoneAuthViewModel.
    const val PHONE_AUTH = "phone_auth"
    const val LOGIN_PHONE = "login_phone"
    const val OTP = "otp"

    const val HOME = "home"
    const val ACTIVITY = "activity"
    const val HISTORY = "history"
    const val PROGRESS = "progress"
    const val PROFILE = "profile"
}
