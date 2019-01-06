package utilities

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
 * likeliest key is the one for which the average Hamming distance between successive blocks is the smallest.
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