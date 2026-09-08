package com.fitwithai.di

import android.content.Context
import com.fitwithai.BuildConfig
import com.fitwithai.auth.ActivityProvider
import com.fitwithai.core.database.DatabaseDriverFactory
import com.fitwithai.core.database.databaseModule
import com.fitwithai.core.datastore.AndroidSecureStorage
import com.fitwithai.core.datastore.SecureStorage
import com.fitwithai.core.datastore.datastoreModule
import com.fitwithai.core.network.ApiConfig
import com.fitwithai.core.network.networkModule
import com.fitwithai.core.platform.Platform
import com.fitwithai.core.platform.platformModule
import io.ktor.client.plugins.logging.LogLevel
import com.fitwithai.presentation.di.presentationModule
import com.fitwithai.data.auth.GoogleCredentialProvider
import com.fitwithai.data.auth.GoogleCredentialProviderImpl
import com.fitwithai.data.auth.GoogleLoginRepositoryImpl
import com.fitwithai.data.auth.InstagramLoginRepositoryImpl
import com.fitwithai.data.di.dataModule
import com.fitwithai.domain.repository.GoogleLoginRepository
import com.fitwithai.domain.repository.InstagramLoginRepository
import com.fitwithai.domain.usecase.GoogleLoginUseCase
import com.fitwithai.domain.usecase.InstagramLoginUseCase
import com.fitwithai.ui.screens.login.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import com.russhwolf.settings.SharedPreferencesSettings
import com.russhwolf.settings.Settings
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Android-only platform bindings: everything that needs a `Context` or the Firebase
 * Android SDK. Shared graph lives in [databaseModule], [datastoreModule], [dataModule].
 */
val androidPlatformModule = module {
    single { DatabaseDriverFactory(androidContext()) }
    single<SecureStorage> { AndroidSecureStorage(androidContext()) }
    single<Settings> {
        SharedPreferencesSettings(
            androidContext().getSharedPreferences("fitwithai_settings", Context.MODE_PRIVATE),
        )
    }
    single {
        ApiConfig(
            baseUrl = if (BuildConfig.DEBUG) "http://10.0.2.2:8080/" else "https://api.fitwithai.app/",
            platformName = get<Platform>().name,
            appVersion = BuildConfig.VERSION_NAME,
            logLevel = if (BuildConfig.DEBUG) LogLevel.BODY else LogLevel.NONE,
        )
    }
}

/** Android auth + presentation wiring (Firebase, Credential Manager, login screen). */
val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { ActivityProvider() }

    single<GoogleLoginRepository> {
        GoogleLoginRepositoryImpl(firebaseAuth = get(), tokenManager = get())
    }
    single<InstagramLoginRepository> {
        InstagramLoginRepositoryImpl(
            firebaseAuth = get(),
            tokenManager = get(),
            activityProvider = get(),
        )
    }
    single<GoogleCredentialProvider> { GoogleCredentialProviderImpl(androidContext()) }

    factory { GoogleLoginUseCase(get()) }
    factory { InstagramLoginUseCase(get()) }

    viewModel { LoginViewModel(get(), get()) }
}

val koinModules = listOf(
    androidPlatformModule,
    platformModule,
    databaseModule,
    datastoreModule,
    networkModule,
    dataModule,
    presentationModule,
    appModule,
)
