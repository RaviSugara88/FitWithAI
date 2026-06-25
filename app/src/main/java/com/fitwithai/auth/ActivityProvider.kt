package com.fitwithai.auth

import android.app.Activity
import java.lang.ref.WeakReference

/**
 * Holds a weak reference to the current foreground Activity so platform OAuth flows
 * (e.g. Firebase Instagram sign-in) can run without leaking the Activity into shared code.
 * Set from [com.fitwithai.app.MainActivity].
 */
class ActivityProvider {
    private var ref: WeakReference<Activity>? = null

    fun set(activity: Activity?) {
        ref = activity?.let { WeakReference(it) }
    }

    fun current(): Activity =
        ref?.get() ?: error("No active Activity available for the sign-in flow")
}
