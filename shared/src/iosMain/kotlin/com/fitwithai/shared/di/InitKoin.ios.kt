package com.fitwithai.shared.di

import com.fitwithai.core.database.DatabaseDriverFactory
import com.fitwithai.core.datastore.IosSecureStorage
import com.fitwithai.core.datastore.SecureStorage
import com.fitwithai.core.network.ApiConfig
import com.fitwithai.core.platform.Platform
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

/**
 * iOS platform Koin module — the Apple counterpart of Android's `androidPlatformModule`.
 * Supplies the platform-bound singletons the shared graph leaves abstract.
 * Verified on macOS (Apple targets cannot be linked on Windows).
 */
val iosPlatformModule = module {
    single { DatabaseDriverFactory() }
    single<SecureStorage> { IosSecureStorage() }
    single<Settings> { NSUserDefaultsSettings(NSUserDefaults.standardUserDefaults) }
    single {
        ApiConfig(
            baseUrl = "https://api.fitwithai.app/",
            platformName = get<Platform>().name,
            appVersion = "1.0",
        )
    }
}

/**
 * Swift entry point: `InitKoinKt.doInitKoin()`. Starts Koin with the shared graph plus the
 * iOS platform module.
 */
fun initKoin(appDeclaration: KoinAppDeclaration = {}): KoinApplication = startKoin {
    appDeclaration()
    modules(sharedModules + iosPlatformModule)
}
