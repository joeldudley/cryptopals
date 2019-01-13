package ciphers.toyciphers

import ciphers.AesEcbCipher
import ciphers.Cipher
import utilities.base64ToBytes

/**
 * A cipher that encrypts the plaintext with AES in ECB mode with an unknown fixed key after appending unknown fixed
 * trailing bytes.
 */

class ECBUnknownKeyUnknownTrailingBytesCipher : Cipher {
    override val key: ByteArray get() = throw IllegalStateException("The key of this cipher is fixed by unknown.")
    private val unknownKey = "FREEZE CONSTRUCT".toByteArray()
    private val unknownTrailingBytes = ("Um9sbGluJyBpbiBteSA1LjAKV2l0aCBteSByYWctdG9wIGRvd24gc28gbXkgaGFpciBjYW4gYmx" +
            "vdwpUaGUgZ2lybGllcyBvbiBzdGFuZGJ5IHdhdmluZyBqdXN0IHRvIHNheSBoaQpEaWQgeW91IHN0b3A/IE5vLCBJIGp1c3QgZHJvdm" +
            "UgYnkK").base64ToBytes()
    private val cipher = AesEcbCipher()

    init {
        cipher.key = unknownKey
    }

    override fun encrypt(plaintext: ByteArray): ByteArray {
        return cipher.encrypt(plaintext + unknownTrailingBytes)
    }

    override fun decrypt(ciphertext: ByteArray): ByteArray {
        val plaintextWithTrailingBytes = cipher.decrypt(ciphertext)

        // TODO: Assuming it was correct to pad with null bytes in AES ECB.
        val firstPaddingByteIndex = plaintextWithTrailingBytes.indexOf(0.toByte())
        val firstTrailingByteIndex = firstPaddingByteIndex - unknownTrailingBytes.size
        return plaintextWithTrailingBytes.sliceArray(0 until firstTrailingByteIndex)
    }
}
