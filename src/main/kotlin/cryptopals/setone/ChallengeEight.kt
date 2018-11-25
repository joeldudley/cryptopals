package cryptopals.setone

import cryptopals.Challenge
import utilities.base64ToBytes
import utilities.hexToBytes
import utilities.toAscii
import java.io.File
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

object ChallengeEight : Challenge(1, 8) {
    override fun passes(): Boolean {
        val providedHexStrings = File("src/main/resources/challengedata/8.txt").readLines()

        val bytesList = providedHexStrings.map { it.hexToBytes() }

        // TODO: Below seems to work - check it.
        val repetitionsList = mutableListOf<Int>()
        bytesList.forEach { bytes ->
            var repetitions = 0
            // TODO: Using a set isn't very efficient due to the way byte array equality works. Working around it for
            // TODO: now using toString().
            val dict = mutableSetOf<String>()

            (0 until 10).forEach { idx ->
                // Need to multiple the indexes by 16.
                val block = bytes.slice((idx * 16) until (idx + 1) * 16).toString()
                if (block in dict) repetitions += 1
                else dict.add(block)
            }

            repetitionsList.add(repetitions)
        }

        println(repetitionsList.max())

        println("ab".toByteArray().contentEquals("ab".toByteArray()))

        return true
    }
}

fun main(args: Array<String>) {
    ChallengeEight.passes()
}
