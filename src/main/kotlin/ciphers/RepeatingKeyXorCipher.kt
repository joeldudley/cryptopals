package ciphers

import kotlin.experimental.xor

class RepeatingKeyXorCipher : Cipher {
    override var key: ByteArray = ByteArray(0) { 0.toByte() }

    override fun encrypt(plaintext: ByteArray): ByteArray {
        val encryptedBytes = ByteArray(plaintext.size)
        plaintext.forEachIndexed { idx, byte ->
            val keyByte = key[idx % key.size]
            encryptedBytes[idx] = keyByte xor byte
        }
        return encryptedBytes
    }

    override fun decrypt(ciphertext: ByteArray): ByteArray {
        // In repeating-key XOR, encryption and decryption are identical operations.
        return encrypt(ciphertext)
    }

}