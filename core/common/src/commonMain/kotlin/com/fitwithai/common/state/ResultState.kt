package com.fitwithai.common.state

sealed interface ResultState<out T> {
    data class Ok<T>(val value: T) : ResultState<T>
    data class Err(val throwable: Throwable) : ResultState<Nothing>
}
