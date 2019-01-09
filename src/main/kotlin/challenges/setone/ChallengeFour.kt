package challenges.setone

import challenges.Challenge
import utilities.hexToBytes
import utilities.likeliestSingleCharXorUsingNgrams
import utilities.ngramFrequencyGenerator
import java.io.File

/*
One of the 60-character strings in this file has been encrypted by single-character XOR.

Find it.

(Your code from #3 should help.)
*/
object ChallengeFour: Challenge(1, 4) {
    override fun passes(): Boolean {
        val providedCiphertexts = File("src/main/resources/challengedata/4.txt").readLines().map { it.hexToBytes() }
        val expectedKey = '5'.toByte()
        val expectedPlaintext = "Now that the party is jumping\n".toByteArray()

        val trigramFrequencyMap = ngramFrequencyGenerator("src/main/resources/t8.shakespeare.txt", 3)
        val possibleKeys = (0..255).map { it.toByte() }

        var maxScore = 0
        var maxScoreKey: Byte = 0
        var maxScoreBytes = ByteArray(0)
        providedCiphertexts.forEach { ciphertext ->
            val (score, key, bytes) = likeliestSingleCharXorUsingNgrams(ciphertext, possibleKeys, trigramFrequencyMap)
            if (score > maxScore) {
                maxScore = score
                maxScoreKey = key
                maxScoreBytes = bytes
            }
        }

        return (maxScoreKey == expectedKey) && (maxScoreBytes.contentEquals(expectedPlaintext))
    }
}
