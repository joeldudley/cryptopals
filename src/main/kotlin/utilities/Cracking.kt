package utilities

import ciphers.Cipher

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
fun encryptWithoutPrefixEcb(plaintext: ByteArray, cipher: Cipher): ByteArray {
    val blocksize = determineBlocksize(cipher)
    val prefixSize = determinePrefixSizeEcb(cipher)

    val bytesToDrop = roundUpToMultiple(prefixSize, blocksize)
    val numberOfBytesToAbsorbPrefix = bytesToDrop - prefixSize
    val bytesToAbsorbPrefix = ByteArray(numberOfBytesToAbsorbPrefix) { 0.toByte() }

    val ciphertextWithPrefix = cipher.encrypt(bytesToAbsorbPrefix + plaintext)
    return ciphertextWithPrefix.sliceArray(bytesToDrop until ciphertextWithPrefix.size)
}

/**
 * Determine the size of an ECB cipher's prefix.
 */
fun determinePrefixSizeEcb(cipher: Cipher): Int {
    val blocksize = determineBlocksize(cipher)
    val fullPrefixBlocks = determineFullPrefixBlocksEcb(cipher)

    // We can use any bytes that won't be identical to the padding byte.
    val ciphertextOfStartOfHeadingBlock = determineCiphertextOfRepeatedBlockEcb(cipher, 1.toByte())
    var additionalPlaintextBytes = 0
    // We establish how many bytes have to be added to get the ciphertext block we are looking for. This indicates by
    // how many bytes the prefix falls short of another full block.
    while (true) {
        val plaintextSize = blocksize + additionalPlaintextBytes
        val plaintext = ByteArray(plaintextSize) { 1.toByte() }
        val ciphertext = cipher.encrypt(plaintext)
        val ciphertextChunks = ciphertext.chunk(blocksize)
        if (ciphertextChunks.any { chunk -> chunk.contentEquals(ciphertextOfStartOfHeadingBlock) })
            break
        additionalPlaintextBytes += 1
    }

    return if (additionalPlaintextBytes == 0) {
        // If we had to add zero bytes, it means the prefix exactly occupied a number of full blocks.
        fullPrefixBlocks * blocksize
    } else {
        (fullPrefixBlocks * blocksize) + (blocksize - additionalPlaintextBytes)
    }
}

/**
 * Determine whether the [cipher] is operating in ECB or CBC mode.
 */
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

/**
 * Determine the number of blocks an ECB cipher's prefix completely occupies.
 */
private fun determineFullPrefixBlocksEcb(cipher: Cipher): Int {
    val blocksize = determineBlocksize(cipher)

    // We create two ciphertexts from different plaintexts.
    val ciphertextOne = cipher.encrypt(ByteArray(0))
    // We can use any bytes that won't be identical to the padding byte.
    val ciphertextTwo = cipher.encrypt(ByteArray(1) { 1.toByte() })

    val ciphertextOneBlocks = ciphertextOne.chunk(blocksize)
    val ciphertextTwoBlocks = ciphertextTwo.chunk(blocksize)

    var fullPrefixBlocks = 0
    // Because the plaintexts are different, any identical leading blocks must be completely made up of the prefix.
    for ((chunkOne, chunkTwo) in ciphertextOneBlocks.zip(ciphertextTwoBlocks)) {
        if (chunkOne.contentEquals(chunkTwo)) fullPrefixBlocks += 1
        else break
    }

    return fullPrefixBlocks
}

/**
 * Despite a possible prefix, determine how an ECB cipher would encrypt a block of a given byte repeating.
 */
private fun determineCiphertextOfRepeatedBlockEcb(cipher: Cipher, byte: Byte): ByteArray {
    val blocksize = determineBlocksize(cipher)
    val twoBlocksOfBytes = ByteArray(blocksize * 2) { byte }
    val ciphertext = cipher.encrypt(twoBlocksOfBytes)

    val fullPrefixBlocks = determineFullPrefixBlocksEcb(cipher)
    // We know the full prefix blocks contain part of the prefix. The following block might also contain part of the
    // prefix. So we take the block after that.
    val bytesToDrop = (fullPrefixBlocks + 1) * blocksize
    return ciphertext.sliceArray(bytesToDrop until bytesToDrop + blocksize)
}
