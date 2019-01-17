package ciphers.toyciphers

import ciphers.AesCbcCipher
import ciphers.AesEcbCipher
import ciphers.Cipher
import utilities.base64ToBytes
import kotlin.random.Random

/**
 * A cipher that encrypts the plaintext with AES in either ECB or CBC mode with an unknown key, an unknown prefix and
 * an unknown suffix. Cannot be decrypted without further information on the mode used.
 */
class UnknownModeAndKeyAndPrefixAndSuffixCipher : Cipher {
    override var key = Random.nextBytes(16)
        get() = throw IllegalStateException("The key of this cipher is unknown.")
    private val unknownPrefix: ByteArray
    private val unknownSuffix: ByteArray
    private val ecbCipher = AesEcbCipher()
    private val cbcCipher = AesCbcCipher()
    var lastModeUsedIsEcb = false

    init {
        val unknownPrefixLength = Random.nextInt(0, 100)
        unknownPrefix = Random.nextBytes(unknownPrefixLength)
        val unknownSuffixLength = Random.nextInt(0, 100)
        unknownSuffix = Random.nextBytes(unknownSuffixLength)
    }

    override fun encrypt(plaintext: ByteArray): ByteArray {
        val useEcb = Random.nextBoolean()
        if (useEcb) {
            lastModeUsedIsEcb = true
            return ecbCipher.encrypt(unknownPrefix + plaintext + unknownSuffix)
        } else {
            lastModeUsedIsEcb = false
            return cbcCipher.encrypt(unknownPrefix + plaintext + unknownSuffix)
        }
    }

    override fun decrypt(ciphertext: ByteArray): ByteArray {
        throw NotImplementedError()
    }
}
