package com.fitwithai.data.di

import com.fitwithai.common.dispatcher.DefaultDispatcherProvider
import com.fitwithai.common.dispatcher.DispatcherProvider
import com.fitwithai.data.workout.WorkoutRepositoryImpl
import com.fitwithai.domain.repository.WorkoutRepository
import com.fitwithai.domain.usecase.workout.ObserveWorkoutsUseCase
import com.fitwithai.domain.usecase.workout.UpsertWorkoutUseCase
import org.koin.dsl.module

/** Shared data graph: repositories + their use cases. Depends on [databaseModule] for the DB. */
val dataModule = module {
    single<DispatcherProvider> { DefaultDispatcherProvider() }
    single<WorkoutRepository> { WorkoutRepositoryImpl(db = get(), dispatchers = get()) }
    single { ObserveWorkoutsUseCase(get()) }
    single { UpsertWorkoutUseCase(get()) }
}
