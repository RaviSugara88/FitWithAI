package com.fitwithai.data.di

import com.fitwithai.common.dispatcher.DefaultDispatcherProvider
import com.fitwithai.common.dispatcher.DispatcherProvider
import com.fitwithai.core.network.TokenProvider
import com.fitwithai.data.auth.PhoneAuthRepositoryImpl
import com.fitwithai.data.remote.AuthRemoteDataSource
import com.fitwithai.data.remote.DefaultTokenProvider
import com.fitwithai.data.remote.WorkoutRemoteDataSource
import com.fitwithai.data.workout.WorkoutRepositoryImpl
import com.fitwithai.domain.repository.PhoneAuthRepository
import com.fitwithai.domain.repository.WorkoutRepository
import com.fitwithai.domain.usecase.workout.ObserveWorkoutsUseCase
import com.fitwithai.domain.usecase.workout.UpsertWorkoutUseCase
import org.koin.dsl.module

/** Shared data graph: repositories, remote sources, and their use cases. */
val dataModule = module {
    single<DispatcherProvider> { DefaultDispatcherProvider() }

    // Remote layer (needs the authed HttpClient from networkModule + platform ApiConfig).
    single<TokenProvider> { DefaultTokenProvider(tokenManager = get(), config = get()) }
    single { WorkoutRemoteDataSource(client = get()) }
    single { AuthRemoteDataSource(config = get(), apiClient = get()) }
    single<PhoneAuthRepository> { PhoneAuthRepositoryImpl(authRemote = get(), tokenManager = get()) }

    single<WorkoutRepository> {
        WorkoutRepositoryImpl(db = get(), dispatchers = get(), remote = get(), prefs = get())
    }
    single { ObserveWorkoutsUseCase(get()) }
    single { UpsertWorkoutUseCase(get()) }
}
