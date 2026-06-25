package com.fitwithai.core.database

import org.koin.dsl.module

/**
 * Shared database graph. The [DatabaseDriverFactory] is platform-provided (it needs a
 * `Context` on Android), so it must be supplied by the Android/iOS platform Koin module.
 */
val databaseModule = module {
    single { createDatabase(get()) }
}
