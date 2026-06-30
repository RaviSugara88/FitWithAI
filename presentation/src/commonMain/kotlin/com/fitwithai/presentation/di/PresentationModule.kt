package com.fitwithai.presentation.di

import com.fitwithai.presentation.viewmodel.WorkoutViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Shared presentation graph: multiplatform ViewModels resolved via koin-compose-viewmodel.
 * Depends on the use cases bound by `dataModule`.
 */
val presentationModule = module {
    viewModelOf(::WorkoutViewModel)
}
