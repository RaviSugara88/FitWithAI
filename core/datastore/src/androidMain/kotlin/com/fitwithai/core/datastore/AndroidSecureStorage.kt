package com.fitwithai.core.datastore

import android.content.Context

/**
 * Android [SecureStorage] backed by private SharedPreferences holding AES-GCM ciphertext,
 * with the key sealed in the AndroidKeyStore (see [EncryptionManager]).
 */
class AndroidSecureStorage(context: Context) : SecureStorage {

    private val prefs = context.getSharedPreferences("fitwithai_secure_store", Context.MODE_PRIVATE)

    override suspend fun putString(key: String, value: String) {
        prefs.edit().putString(key, EncryptionManager.encrypt(value)).apply()
    }

    override suspend fun getString(key: String): String? =
        prefs.getString(key, null)?.let { EncryptionManager.decrypt(it) }

    override suspend fun remove(key: String) {
        prefs.edit().remove(key).apply()
    }
}
