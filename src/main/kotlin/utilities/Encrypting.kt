package utilities

import ciphers.AesCbcCipher
import ciphers.AesEcbCipher
import kotlin.random.Random

/**
 * Encrypt the [plaintext] with AES in either ECB or CBC mode with a random key and random padding.
 */
fun encryptWithAESInCBCOrECBWithRandomKeyAndRandomPadding(plaintext: ByteArray): ByteArray {
    val leadingRandomBytesLength = Random.nextInt(5, 11)
    val trailingRandomBytesLength = Random.nextInt(5, 11)
    val leadingRandomBytes = Random.nextBytes(leadingRandomBytesLength)
    val trailingRandomBytes = Random.nextBytes(trailingRandomBytesLength)

    val paddedPlaintext = leadingRandomBytes + plaintext + trailingRandomBytes

    val randomKey = Random.nextBytes(16)
    val useCBC = Random.nextBoolean()

    return if (useCBC) {
        val cipher = AesCbcCipher()
        cipher.key = randomKey
        cipher.iv = Random.nextBytes(16)
        cipher.encrypt(paddedPlaintext)
    } else {
        val cipher = AesEcbCipher()
        cipher.key = randomKey
        cipher.encrypt(paddedPlaintext)
    }
}

/**
 * Encrypt the [plaintext] with AES in either ECB or CBC mode with a fixed key and trailing bytes.
 */
fun encryptWithAESInECBModeWithFixedKeyAndFixedTrailingBytes(plaintext: ByteArray): ByteArray {
    val key = "FREEZE CONSTRUCT".toByteArray()
    val trailingBytes = ("Um9sbGluJyBpbiBteSA1LjAKV2l0aCBteSByYWctdG9wIGRvd24gc28gbXkgaGFpciBjYW4gYmxvdwpUaGUgZ2lyb" +
            "GllcyBvbiBzdGFuZGJ5IHdhdmluZyBqdXN0IHRvIHNheSBoaQpEaWQgeW91IHN0b3A/IE5vLCBJIGp1c3QgZHJvdmUgYnkK")
            .base64ToBytes()
    val cipher = AesCbcCipher()
    cipher.key = key
    cipher.iv = Random.nextBytes(16)
    return cipher.encrypt(plaintext + trailingBytes)
}
