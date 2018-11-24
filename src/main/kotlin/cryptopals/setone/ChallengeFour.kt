package cryptopals.setone

import cryptopals.Challenge
import utilities.hexToBytes
import utilities.likeliestSingleCharXorUsingNgrams
import utilities.ngramFrequencyGenerator
import java.io.File

object ChallengeFour: Challenge(1, 4) {
    override fun passes(): Boolean {
        val providedHexStrings = File("src/main/resources/challengedata/4.txt").readLines()
        val expectedKey = '5'.toByte()
        val expectedAsciiString = "Now that the party is jumping\n"

        val bytesList = providedHexStrings.map { it.hexToBytes() }

        val trigramFrequencyMap = ngramFrequencyGenerator("src/main/resources/t8.shakespeare.txt", 3)
        val possibleKeys = (0..255).map { it.toByte() }

        var maxScore = 0
        var maxScoreKey: Byte = 0
        var maxScoreAsciiString = ""
        bytesList.forEach { bytes ->
            val (score, key, asciiString) = likeliestSingleCharXorUsingNgrams(bytes, possibleKeys, trigramFrequencyMap)
            if (score > maxScore) {
                maxScore = score
                maxScoreKey = key
                maxScoreAsciiString = asciiString
            }
        }

        return (maxScoreKey == expectedKey) && (maxScoreAsciiString == expectedAsciiString)
    }
}
