package com.fitwithai.shared.di

import com.fitwithai.core.database.databaseModule
import com.fitwithai.core.datastore.datastoreModule
import com.fitwithai.core.platform.platformModule
import com.fitwithai.data.di.dataModule
import com.fitwithai.presentation.di.presentationModule
import org.koin.core.module.Module

/**
 * The platform-agnostic Koin graph shared by every target. A host (Android `FitWithAiApp` or
 * iOS [initKoin]) appends its own platform module — the one that supplies `Context`/Keychain
 * -bound singletons (`DatabaseDriverFactory`, `SecureStorage`, `Settings`).
 */
val sharedModules: List<Module> = listOf(
    platformModule,
    databaseModule,
    datastoreModule,
    dataModule,
    presentationModule,
)
