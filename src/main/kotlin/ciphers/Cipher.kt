package ciphers

interface Cipher {
    var key: ByteArray
    fun encrypt(plaintext: ByteArray): ByteArray
    fun decrypt(ciphertext: ByteArray): ByteArray
}

/**
 * A cipher that requires an initialisation vector.
 */
interface CipherWithIv : Cipher {
    val iv: ByteArray
}
