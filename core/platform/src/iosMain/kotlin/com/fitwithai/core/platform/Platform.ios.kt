package com.fitwithai.core.platform

import platform.UIKit.UIDevice

private class IosPlatform : Platform {
    private val device = UIDevice.currentDevice
    override val name: String = device.systemName
    override val osVersion: String = device.systemVersion
    // UIDevice.name on a simulator still reports the simulated device; the env var below is the
    // reliable simulator tell, but it is not available in common Kotlin/Native without cinterop,
    // so we approximate: simulators report model "Simulator"-suffixed names on modern Xcode.
    override val isPhysicalDevice: Boolean = device.model.endsWith("Simulator").not()
}

actual fun currentPlatform(): Platform = IosPlatform()
