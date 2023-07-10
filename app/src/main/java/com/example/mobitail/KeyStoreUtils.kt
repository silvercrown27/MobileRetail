package com.example.mobitail

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

object KeyStoreUtils {

    private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    private const val KEY_ALIAS = "my_key_alias"
    private const val KEY_SIZE = 256

    fun generateSecretKeyIfNeeded(userId: String): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)

        // Generate a unique key alias based on the user ID
        val userKeyAlias = "$KEY_ALIAS-$userId"

        // Check if the key exists in the keystore
        if (!keyStore.containsAlias(userKeyAlias)) {
            return generateSecretKey(userKeyAlias)
        }

        val key = keyStore.getKey(userKeyAlias, null)
        if (key != null && key is SecretKey) {
            return key
        }

        return generateSecretKey(userKeyAlias)
    }

    private fun generateSecretKey(keyAlias: String): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
        val keyGenSpec = KeyGenParameterSpec.Builder(
            keyAlias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .setKeySize(KEY_SIZE)
            .build()

        keyGenerator.init(keyGenSpec)
        return keyGenerator.generateKey()
    }
}
