package com.fitwithai.core.platform

/**
 * Read-only description of the host device. The seed of `:core:platform` — the module that
 * will hold the expect/actual device-capability abstractions (sensors, BLE, services) as the
 * iOS host matures. Kept deliberately tiny and dependency-free so it compiles for every target.
 */
interface Platform {
    /** Human-readable platform name, e.g. "Android" or "iOS". */
    val name: String

    /** OS version string, e.g. "14" (Android) or "17.4" (iOS). */
    val osVersion: String

    /** False on emulators/simulators, true on real hardware. */
    val isPhysicalDevice: Boolean
}

/** The current host platform. Implemented per target via `actual`. */
expect fun currentPlatform(): Platform
