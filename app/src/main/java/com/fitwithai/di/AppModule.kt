package com.fitwithai.di

import com.fitwithai.auth.TokenStorage
import com.fitwithai.data.auth.GoogleCredentialProvider
import com.fitwithai.data.auth.GoogleCredentialProviderImpl
import com.fitwithai.data.auth.GoogleLoginRepositoryImpl
import com.fitwithai.data.auth.InstagramLoginRepositoryImpl
import com.fitwithai.domain.repository.GoogleLoginRepository
import com.fitwithai.domain.repository.InstagramLoginRepository
import com.fitwithai.domain.usecase.GoogleLoginUseCase
import com.fitwithai.domain.usecase.InstagramLoginUseCase
import com.fitwithai.ui.screens.login.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { TokenStorage(androidContext()) }
    single<GoogleLoginRepository> {
        GoogleLoginRepositoryImpl(
            firebaseAuth = get(),
            tokenStorage = get(),
        )
    }
    single<InstagramLoginRepository> {
        InstagramLoginRepositoryImpl(
            firebaseAuth = get(),
            tokenStorage = get(),
        )
    }
    single<GoogleCredentialProvider> {
        GoogleCredentialProviderImpl(androidContext())
    }
    factory { GoogleLoginUseCase(get()) }
    factory { InstagramLoginUseCase(get()) }
    viewModel { LoginViewModel(get(), get()) }
}
