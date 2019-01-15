package ciphers

import utilities.padToMultipleOf
import utilities.stripPadding
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AesCbcCipher : CipherWithIv {
    override var key: ByteArray = ByteArray(16) { 0.toByte() }
    override var iv: ByteArray = ByteArray(16) { 0.toByte() }

    override fun encrypt(plaintext: ByteArray): ByteArray {
        // TODO: Check that padding with null bytes is correct.
        val paddedPlaintext = plaintext.padToMultipleOf(key.size)

        val secretKeySpec = SecretKeySpec(key, "AES")
        val cipher = javax.crypto.Cipher.getInstance("AES/CBC/NoPadding")
        cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, secretKeySpec, IvParameterSpec(iv))
        return cipher.doFinal(paddedPlaintext)
    }

    override fun decrypt(ciphertext: ByteArray): ByteArray {
        val secretKeySpec = SecretKeySpec(key, "AES")
        val cipher = javax.crypto.Cipher.getInstance("AES/CBC/NoPadding")
        cipher.init(javax.crypto.Cipher.DECRYPT_MODE, secretKeySpec, IvParameterSpec(iv))
        return cipher.doFinal(ciphertext).stripPadding()
    }

}