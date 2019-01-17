package utilities

import ciphers.Cipher
import ciphers.toyciphers.ECBUnknownKeyAndPrefixAndSuffixCipher
import ciphers.toyciphers.ECBUnknownKeyAndSuffixCipher

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
fun determineBlocksize(cipher: Cipher): Int {
    val initialPlaintext = ByteArray(1) { 0.toByte() }
    val initalCiphertext = cipher.encrypt(initialPlaintext)
    val initialCiphertextLength = initalCiphertext.size

    for (i in 2..Int.MAX_VALUE) {
        val plaintext = ByteArray(i) { 0.toByte() }
        val ciphertext = cipher.encrypt(plaintext)
        // If the ciphertext size jumps, we know that the size of the jump is the size of a single block.
        if (initialCiphertextLength != ciphertext.size) return ciphertext.size - initialCiphertextLength
    }

    throw IllegalStateException("The block size could not be determined.")
}

/**
 * Encrypt the plaintext without the cipher's prefix (if it has one).
 */
fun encryptWithoutPrefix(plaintext: ByteArray, cipher: Cipher): ByteArray {
    val blocksize = determineBlocksize(cipher)
    // What's important here is that the block is made up of bytes that won't be confused with the padding (which we
    // assumed is made up of null bytes).
    val startOfHeadingBlock = ByteArray(blocksize) { 1.toByte() }
    val ciphertextOfStartOfHeadingBlock = cipher.encrypt(startOfHeadingBlock)

    var plaintextToAbsorbPrefix = ByteArray(0)
    val bytesToDropToRemovePrefix: Int
    while (true) {
        val ciphertext = cipher.encrypt(plaintextToAbsorbPrefix)
        val finalBlock = ciphertext.sliceArray(ciphertext.size - blocksize until ciphertext.size)

        println(ciphertextOfStartOfHeadingBlock.size)

        if (finalBlock.contentEquals(ciphertextOfStartOfHeadingBlock)) {
            bytesToDropToRemovePrefix = ciphertext.size
            break
        }
        plaintextToAbsorbPrefix += 0.toByte()
    }

    val ciphertextWithAbsorbebPrefix = cipher.encrypt(plaintext + plaintextToAbsorbPrefix)
    return ciphertextWithAbsorbebPrefix.sliceArray(bytesToDropToRemovePrefix .. ciphertextWithAbsorbebPrefix.size)
}

/**
 * Determine the length of a cipher's prefix (if it has one) in terms of the number of blocks it completely occupies
 * (if the prefix is not a multiple of the blocksize, it will also consume n bytes of the next blocks where n <
 * blocksize).
 */
fun determineFullPrefixBlocks(cipher: Cipher): Int {
    val blocksize = determineBlocksize(cipher)

    val ciphertextOne = cipher.encrypt(ByteArray(0))
    // We use the start-of-heading byte so that its ciphertext isn't identical to the padding's.
    val ciphertextTwo = cipher.encrypt(ByteArray(1) { 1.toByte() })

    val ciphertextOneBlocks = ciphertextOne.chunk(blocksize)
    val ciphertextTwoBlocks = ciphertextTwo.chunk(blocksize)

    var fullPrefixBlocks = 0
    // The plaintexts are different, so any identical leading blocks must be completely made up of the prefix.
    for ((chunkOne, chunkTwo) in ciphertextOneBlocks.zip(ciphertextTwoBlocks)) {
        if (chunkOne.contentEquals(chunkTwo)) fullPrefixBlocks += 1
        else break
    }

    return fullPrefixBlocks
}

fun main(args: Array<String>) {
    repeat(10) {
        val cipher = ECBUnknownKeyAndPrefixAndSuffixCipher()
        println(determineFullPrefixBlocks(cipher))
        println()
    }
}

/**
 * Determine whether the [cipher] is operating in ECB or CBC mode.
 */
// TODO: Can we do this without providing the blocksize?
fun usesEcbMode(cipher: Cipher): Boolean {
    val blocksize = determineBlocksize(cipher)

    // Every plaintext block is the same, meaning that every ciphertext block will be the same too if we're using ECB
    // mode.
    val plaintext = ByteArray(320) { 0.toByte() }
    val ciphertext = cipher.encrypt(plaintext)
    val ciphertextBlocks = ciphertext.chunk(blocksize)

    // We skip over the block potentially containing the IV.
    // We can detect ECB mode by the repeating blocks for the same plaintext.
    return (ciphertextBlocks[1].contentEquals(ciphertextBlocks[2])
            && ciphertextBlocks[2].contentEquals(ciphertextBlocks[3]))
}
