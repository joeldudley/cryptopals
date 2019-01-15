package ciphers.toyciphers

import ciphers.AesCbcCipher
import ciphers.AesEcbCipher
import ciphers.Cipher
import kotlin.random.Random

/**
 * A cipher that encrypts the plaintext with AES in either ECB or CBC mode with a random key and random padding. It has
 * no decrypt mode.
 */
class RandomModeRandomKeyRandomPaddingCipher : Cipher {
    override var key = ByteArray(0)
        get() = throw IllegalStateException("The key of this cipher is set randomly.")
    // Used in Cryptopals challenge eleven to check our ECB or CBC oracle is working.
    var lastModeUsedIsEcb = false
    private val ecbCipher = AesEcbCipher()
    private val cbcCipher = AesCbcCipher()

    override fun encrypt(plaintext: ByteArray): ByteArray {
        val leadingRandomBytesLength = Random.nextInt(5, 11)
        val trailingRandomBytesLength = Random.nextInt(5, 11)
        val leadingRandomBytes = Random.nextBytes(leadingRandomBytesLength)
        val trailingRandomBytes = Random.nextBytes(trailingRandomBytesLength)

        val paddedPlaintext = leadingRandomBytes + plaintext + trailingRandomBytes

        val randomKey = Random.nextBytes(16)
        val useECB = Random.nextBoolean()

        return if (useECB) {
            lastModeUsedIsEcb = true
            ecbCipher.key = randomKey
            ecbCipher.encrypt(paddedPlaintext)
        } else {
            lastModeUsedIsEcb = false
            cbcCipher.key = randomKey
            cbcCipher.iv = Random.nextBytes(16)
            cbcCipher.encrypt(paddedPlaintext)
        }
    }

    override fun decrypt(ciphertext: ByteArray) =
            throw IllegalStateException("The cipher does not provide a decrypt mode.")
}