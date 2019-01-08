package utilities

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.xor

fun encryptWithRepeatingKeyXor(bytes: ByteArray, key: ByteArray): ByteArray {
    val encryptedBytes = ByteArray(bytes.size)
    bytes.forEachIndexed { idx, asciiStringByte ->
        val keyByte = key[idx % key.size]
        encryptedBytes[idx] = keyByte xor asciiStringByte
    }
    return encryptedBytes
}

/**
 * Find the likeliest key size between 2 and [maxKeySize] that was used to encrypt [bytes] using repeating-key XOR. The
 * likeliest key size is the one for which the average Hamming distance between successive blocks is the smallest.
 */
fun findRepeatingKeyXorKeySize(bytes: ByteArray, maxKeySize: Int): Int {
    var repeatingXorKeySize = 0
    var minAverageHammingDistance = Double.MAX_VALUE
    // The number of blocks we can use to calculate the average Hamming distance.
    val totalBlocks = bytes.size / maxKeySize

    (2..maxKeySize).forEach { size ->
        val totalHammingDistance = (0 until totalBlocks - 1).sumBy { blockIdx ->
            val chunkOne = bytes.slice(blockIdx * size..(blockIdx + 1) * size).toByteArray()
            val chunkTwo = bytes.slice((blockIdx + 1) * size..(blockIdx + 2) * size).toByteArray()
            hammingDistance(chunkOne, chunkTwo)
        }

        val averageHammingDistance = totalHammingDistance/1.0/size

        if (averageHammingDistance < minAverageHammingDistance) {
            repeatingXorKeySize = size
            minAverageHammingDistance = averageHammingDistance
        }
    }

    return repeatingXorKeySize
}

/**
 * Encrypt the [plaintext] with AES in ECB mode with [key].
 */
fun encryptWithAESInECBMode(plaintext: ByteArray, key: ByteArray): ByteArray {
    val secretKeySpec = SecretKeySpec(key, "AES")
    val cipher = Cipher.getInstance("AES/ECB/NoPadding")
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
    return cipher.doFinal(plaintext)
}

/**
 * Decrypt the [ciphertext] with AES in ECB mode with [key].
 */
fun decryptWithAESInECBMode(ciphertext: ByteArray, key: ByteArray): ByteArray {
    val secretKeySpec = SecretKeySpec(key, "AES")
    val cipher = Cipher.getInstance("AES/ECB/NoPadding")
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
    return cipher.doFinal(ciphertext)
}

/**
 * Encrypt the [plaintext] with AES in CBC mode with [key].
 */
fun encryptWithAESInCBCMode(plaintext: ByteArray, key: ByteArray, iv: ByteArray): ByteArray {
    // TODO: Check that padding with null bytes is correct.
    val paddedPlaintext = if (plaintext.size % key.size == 0) {
        plaintext
    } else {
        val paddingLength = key.size - (plaintext.size % key.size)
        plaintext.pad(paddingLength, 0.toByte())
    }

    val plaintextBlocks = paddedPlaintext.toList().chunked(key.size).map { it.toByteArray() }
    // TODO: Is it efficient enough to keep creating and copying a new array?
    var ciphertext = ByteArray(0)
    var ivOrPrecedingCiphertextBlock = iv

    plaintextBlocks.forEach { plaintextBlock ->
        val input = plaintextBlock.xor(ivOrPrecedingCiphertextBlock)
        val ciphertextBlock = encryptWithAESInECBMode(input, key)
        ivOrPrecedingCiphertextBlock = ciphertextBlock
        ciphertext += ciphertextBlock
    }

    return ciphertext
}

/**
 * Decrypt the [ciphertext] with AES in CBC mode with [key].
 */
fun decryptWithAESInCBCMode(ciphertext: ByteArray, key: ByteArray, iv: ByteArray): ByteArray {
    val ciphertextBlocks = ciphertext.toList().chunked(key.size).map { it.toByteArray() }
    // TODO: Is it efficient enough to keep creating and copying a new array?
    var plaintext = ByteArray(0)
    var ivOrPrecedingCiphertextBlock = iv

    ciphertextBlocks.forEach { ciphertextBlock ->
        val output = decryptWithAESInECBMode(ciphertextBlock, key)
        val plaintextBlock = output.xor(ivOrPrecedingCiphertextBlock)
        ivOrPrecedingCiphertextBlock = ciphertextBlock
        plaintext += plaintextBlock
    }

    // TODO: Should decryption also strip the padding?

    return plaintext
}

fun main(args: Array<String>) {
    // TODO: Remove this and check CBC works in the context of the 10th challenge.
    val plaintext = "JOELJOELJOELJOELboelboelboelboe".toByteArray()
    val iv = ByteArray(16) { 0.toByte() }
    val key = "YELLOW SUBMARINE".toByteArray()

    val encryption = encryptWithAESInCBCMode(plaintext, key, iv)
    val decryption = decryptWithAESInCBCMode(encryption, key, iv)

    println(plaintext.toHex())
    println(encryption.toHex())
    println(decryption.toHex())
}
