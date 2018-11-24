package cryptopals.setone

import cryptopals.Challenge
import utilities.base64ToBytes
import utilities.hammingDistance
import java.io.File

object ChallengeSix : Challenge(1, 6) {
    override fun passes(): Boolean {
        val providedEncryptedBase64String = File("src/main/resources/challengedata/6.txt").readText().filter { it != '\n' }
        val encryptedBytes = providedEncryptedBase64String.base64ToBytes()

        // TODO: It looks like the key size is 29. 29 and 58 (29 * 2) have the lowest total normalised Hamming
        // TODO: distances.
        val keySizesAndTotalDistances = (2..58).map { size ->
            var totalHammingDistance = 0
            (0..40).forEach { idx ->
                val chunkOne = encryptedBytes.slice(idx * size..(idx + 1) * size).toByteArray()
                val chunkTwo = encryptedBytes.slice((idx + 1) * size..(idx + 2) * size).toByteArray()
                totalHammingDistance += hammingDistance(chunkOne, chunkTwo)
            }

            Pair(size, totalHammingDistance / 1.0 / size)
        }

        keySizesAndTotalDistances.sortedBy { it.second }.forEach { (size, distance) ->
            println("$size: $distance")
        }

        return true
    }
}

fun main(args: Array<String>) {
    ChallengeSix.run()
}
