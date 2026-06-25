package com.fitwithai.core.datastore

/**
 * Platform-encrypted key/value storage. Android backs this with the AndroidKeyStore
 * (AES-GCM); iOS will back it with the Keychain.
 */
interface SecureStorage {
    suspend fun putString(key: String, value: String)
    suspend fun getString(key: String): String?
    suspend fun remove(key: String)
}
