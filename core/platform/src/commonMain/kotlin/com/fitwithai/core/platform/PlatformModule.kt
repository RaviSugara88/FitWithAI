package com.fitwithai.core.platform

import org.koin.dsl.module

/**
 * Shared platform graph. [currentPlatform] resolves to the target's `actual`, so this module
 * is context-free and lives in the common Koin graph (no platform module needed to supply it).
 */
val platformModule = module {
    single<Platform> { currentPlatform() }
}
