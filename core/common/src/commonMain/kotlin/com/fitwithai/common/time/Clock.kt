package com.fitwithai.common.time

/** Wall-clock time in epoch milliseconds. Implemented per target via `actual`. */
expect fun currentEpochMillis(): Long
