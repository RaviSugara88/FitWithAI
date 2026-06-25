package com.fitwithai.common.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

// Kotlin/Native has no dedicated IO dispatcher; Default is the idiomatic fallback.
actual val platformIoDispatcher: CoroutineDispatcher = Dispatchers.Default
