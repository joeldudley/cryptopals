package ciphers.toyciphers

import ciphers.AesEcbCipher
import ciphers.Cipher
import utilities.base64ToBytes
import kotlin.random.Random

/**
 * A cipher that encrypts the plaintext with AES in ECB mode with an unknown key, an unknown prefix and an unknown
 * suffix.
 */
class ECBUnknownKeyAndPrefixAndSuffixCipher : Cipher {
    override var key = "YELLOW SUBMARINE".toByteArray()
        get() = throw IllegalStateException("The key of this cipher is unknown.")
    private val unknownPrefix: ByteArray
    private val unknownSuffix = ("Um9sbGluJyBpbiBteSA1LjAKV2l0aCBteSByYWctdG9wIGRvd24gc28gbXkgaGFpciBjYW4gYmx" +
            "vdwpUaGUgZ2lybGllcyBvbiBzdGFuZGJ5IHdhdmluZyBqdXN0IHRvIHNheSBoaQpEaWQgeW91IHN0b3A/IE5vLCBJIGp1c3QgZHJvdm" +
            "UgYnkK").base64ToBytes()
    private val cipher = AesEcbCipher()

    init {
        val unknownPrefixLength = Random.nextInt(0, 100)
        println(unknownPrefixLength)
        unknownPrefix = Random.nextBytes(unknownPrefixLength)
    }

    override fun encrypt(plaintext: ByteArray): ByteArray {
        return cipher.encrypt(unknownPrefix + plaintext + unknownSuffix)
    }

    override fun decrypt(ciphertext: ByteArray): ByteArray {
        val plaintextWithPrefixAndSuffix = cipher.decrypt(ciphertext)

        // TODO: Assuming it was correct to pad with null bytes in AES ECB.
        val firstPaddingByteIndex = if (0.toByte() in plaintextWithPrefixAndSuffix) {
            plaintextWithPrefixAndSuffix.indexOf(0.toByte())
        } else {
            plaintextWithPrefixAndSuffix.size
        }
        val firstSuffixByteIndex = firstPaddingByteIndex - unknownSuffix.size
        return plaintextWithPrefixAndSuffix.sliceArray(unknownPrefix.size until firstSuffixByteIndex)
    }
}
