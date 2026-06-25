package com.fitwithai.core.database

import app.cash.sqldelight.db.SqlDriver

/** Creates the platform [SqlDriver] for the shared SQLDelight database. */
expect class DatabaseDriverFactory {
    fun create(): SqlDriver
}

/** Convenience builder so callers don't depend on the generated schema directly. */
fun createDatabase(driverFactory: DatabaseDriverFactory): FitWithAiDatabase =
    FitWithAiDatabase(driverFactory.create())
