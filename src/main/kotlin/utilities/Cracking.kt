package utilities

import ciphers.Cipher
import java.lang.IllegalStateException

/**
 * Find the likeliest key size between 2 and [maxKeySize] that was used to encrypt [ciphertext] using repeating-key XOR. The
 * likeliest key size is the one for which the average Hamming distance between successive blocks is the smallest.
 */
fun findRepeatingKeyXorKeySize(ciphertext: ByteArray, maxKeySize: Int): Int {
    var repeatingXorKeySize = 0
    var minAverageHammingDistance = Double.MAX_VALUE
    // The number of blocks we can use to calculate the average Hamming distance.
    val totalBlocks = ciphertext.size / maxKeySize

    (2..maxKeySize).forEach { size ->
        val totalHammingDistance = (0 until totalBlocks - 1).sumBy { blockIdx ->
            val chunkOne = ciphertext.slice(blockIdx * size..(blockIdx + 1) * size).toByteArray()
            val chunkTwo = ciphertext.slice((blockIdx + 1) * size..(blockIdx + 2) * size).toByteArray()
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
 * Determine the block size used by a cipher.
 */
// TODO: This approach does not work is the cipher uses random-size padding. Is that an issue?
fun determineBlockSize(cipher: Cipher): Int {
    val initialPlaintext = ByteArray(1) { 0.toByte() }
    val initalCiphertext = cipher.encrypt(initialPlaintext)
    val initialCiphertextLength = initalCiphertext.size

    for (i in 1..Int.MAX_VALUE) {
        val plaintext = ByteArray(i) { 0.toByte() }
        val ciphertext = cipher.encrypt(plaintext)
        // If the ciphertext size jumps, we know that the size of the jump is the size of a single block.
        if (initialCiphertextLength != ciphertext.size) return ciphertext.size - initialCiphertextLength
    }

    throw IllegalStateException("The block size could not be determined.")
}

/**
 * Determine whether the [cipher] is operating in ECB or CBC mode.
 */
// TODO: Can we do this without providing the blocksize?
fun usesEcbMode(cipher: Cipher, blockSize: Int): Boolean {
    // Every plaintext block is the same, meaning that every ciphertext block will be the same too if we're using ECB
    // mode.
    val plaintext = ByteArray(320) { 0.toByte() }
    val ciphertext = cipher.encrypt(plaintext)
    val ciphertextBlocks = ciphertext.chunk(blockSize)

    // We skip over the block potentially containing the IV.
    // We can detect ECB mode by the repeating blocks for the same plaintext.
    return (ciphertextBlocks[1].contentEquals(ciphertextBlocks[2])
            && ciphertextBlocks[2].contentEquals(ciphertextBlocks[3]))
}
