package ciphers.toyciphers

import ciphers.AesEcbCipher
import ciphers.Cipher
import utilities.base64ToBytes
import kotlin.random.Random

/**
 * A cipher that encrypts the plaintext with AES in ECB mode with an unknown fixed key after suffixing unknown fixed
 * bytes.
 */

class ECBUnknownKeyAndSuffixCipher : Cipher {
    override var key = Random.nextBytes(16)
        get() = throw IllegalStateException("The key of this cipher is unknown.")
    private val unknownSuffix = ("Um9sbGluJyBpbiBteSA1LjAKV2l0aCBteSByYWctdG9wIGRvd24gc28gbXkgaGFpciBjYW4gYmx" +
            "vdwpUaGUgZ2lybGllcyBvbiBzdGFuZGJ5IHdhdmluZyBqdXN0IHRvIHNheSBoaQpEaWQgeW91IHN0b3A/IE5vLCBJIGp1c3QgZHJvdm" +
            "UgYnkK").base64ToBytes()
    private val cipher = AesEcbCipher()

    override fun encrypt(plaintext: ByteArray): ByteArray {
        return cipher.encrypt(plaintext + unknownSuffix)
    }

    override fun decrypt(ciphertext: ByteArray): ByteArray {
        val plaintextWithSuffix = cipher.decrypt(ciphertext)

        // TODO: Assuming it was correct to pad with null bytes in AES ECB.
        val firstPaddingByteIndex = if (0.toByte() in plaintextWithSuffix) {
            plaintextWithSuffix.indexOf(0.toByte())
        } else {
            plaintextWithSuffix.size
        }
        val firstSuffixByteIndex = firstPaddingByteIndex - unknownSuffix.size
        return plaintextWithSuffix.sliceArray(0 until firstSuffixByteIndex)
    }
}
