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

fun findRepeatingKeyXorKeySize(bytes: ByteArray, maxPossibleKeySize: Int): Int {
    var keySize = 0
    var maxNormalisedHammingDistance = Double.MAX_VALUE
    val blocksToCheck = bytes.size / maxPossibleKeySize
    (2..maxPossibleKeySize).forEach { size ->

        val totalHammingDistance = (0 until blocksToCheck).sumBy { idx ->
            val chunkOne = bytes.slice(idx * size..(idx + 1) * size).toByteArray()
            val chunkTwo = bytes.slice((idx + 1) * size..(idx + 2) * size).toByteArray()
            hammingDistance(chunkOne, chunkTwo)
        }

        val normalisedHammingDistance = totalHammingDistance/1.0/size

        if (normalisedHammingDistance < maxNormalisedHammingDistance) {
            keySize = size
            maxNormalisedHammingDistance = normalisedHammingDistance
        }
    }

    return keySize
}