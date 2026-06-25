package com.fitwithai.common.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual val platformIoDispatcher: CoroutineDispatcher = Dispatchers.IO
