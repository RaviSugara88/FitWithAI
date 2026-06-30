package com.fitwithai

import com.fitwithai.common.dispatcher.DispatcherProvider
import com.fitwithai.core.database.DatabaseDriverFactory
import com.fitwithai.core.database.FitWithAiDatabase
import com.fitwithai.core.database.databaseModule
import com.fitwithai.core.datastore.SecureStorage
import com.fitwithai.core.datastore.datastoreModule
import com.fitwithai.core.platform.platformModule
import com.fitwithai.data.di.dataModule
import com.fitwithai.domain.repository.WorkoutRepository
import com.fitwithai.domain.usecase.workout.ObserveWorkoutsUseCase
import com.fitwithai.domain.usecase.workout.UpsertWorkoutUseCase
import com.fitwithai.presentation.di.presentationModule
import com.russhwolf.settings.Settings
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify

/**
 * Static wiring check for the shared Koin graph (the modules that ship to both Android and iOS).
 * Verifies every constructor dependency is satisfiable without starting Koin or touching a
 * `Context`. Platform-supplied bindings (`DatabaseDriverFactory`, `SecureStorage`, `Settings`)
 * and cross-module definitions are declared as external types.
 */
class SharedKoinGraphTest {

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun `shared koin graph is fully wired`() {
        val external = listOf(
            // platform-provided (Android/iOS platform module)
            DatabaseDriverFactory::class,
            SecureStorage::class,
            Settings::class,
            // provided across module boundaries
            FitWithAiDatabase::class,
            DispatcherProvider::class,
            WorkoutRepository::class,
            ObserveWorkoutsUseCase::class,
            UpsertWorkoutUseCase::class,
        )

        listOf(
            platformModule,
            databaseModule,
            datastoreModule,
            dataModule,
            presentationModule,
        ).forEach { module -> module.verify(extraTypes = external) }
    }
}
