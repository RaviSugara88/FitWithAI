package com.fitwithai.core.datastore

import org.koin.dsl.module

/**
 * Shared datastore graph. [SecureStorage] and `Settings` are platform-provided (they need a
 * `Context` on Android), so the platform Koin module must supply them.
 */
val datastoreModule = module {
    single { TokenManager(get()) }
    single { AppPreferences(get()) }
}
