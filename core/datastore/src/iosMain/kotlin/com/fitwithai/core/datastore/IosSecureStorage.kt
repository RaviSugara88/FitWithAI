package com.fitwithai.core.datastore

/**
 * iOS [SecureStorage]. TODO(M3): back this with the iOS Keychain (Security framework via
 * cinterop). In-memory for now so common code compiles; not used until the iOS host lands.
 */
class IosSecureStorage : SecureStorage {

    private val store = mutableMapOf<String, String>()

    override suspend fun putString(key: String, value: String) {
        store[key] = value
    }

    override suspend fun getString(key: String): String? = store[key]

    override suspend fun remove(key: String) {
        store.remove(key)
    }
}
