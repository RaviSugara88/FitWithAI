package com.fitwithai.common.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/** Abstracts coroutine dispatchers so business logic stays testable and platform-agnostic. */
interface DispatcherProvider {
    val main: CoroutineDispatcher
    val default: CoroutineDispatcher
    val io: CoroutineDispatcher
}

/** Platform-provided IO dispatcher (`Dispatchers.IO` on Android; falls back to Default on iOS). */
expect val platformIoDispatcher: CoroutineDispatcher

class DefaultDispatcherProvider : DispatcherProvider {
    override val main: CoroutineDispatcher = Dispatchers.Main
    override val default: CoroutineDispatcher = Dispatchers.Default
    override val io: CoroutineDispatcher = platformIoDispatcher
}
