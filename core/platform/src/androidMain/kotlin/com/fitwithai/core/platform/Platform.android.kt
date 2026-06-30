package com.fitwithai.core.platform

import android.os.Build

private class AndroidPlatform : Platform {
    override val name: String = "Android"
    override val osVersion: String = Build.VERSION.RELEASE ?: Build.VERSION.SDK_INT.toString()
    override val isPhysicalDevice: Boolean =
        !(Build.FINGERPRINT.startsWith("generic") ||
            Build.FINGERPRINT.contains("emulator", ignoreCase = true) ||
            Build.MODEL.contains("Emulator", ignoreCase = true) ||
            Build.MODEL.contains("sdk", ignoreCase = true) ||
            Build.PRODUCT.contains("sdk", ignoreCase = true))
}

actual fun currentPlatform(): Platform = AndroidPlatform()
