package cryptopals.setone

import cryptopals.Challenge
import utilities.*

object ChallengeThree: Challenge(1, 3) {
    override fun passes(): Boolean {
        val providedHexString = "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736"
        val expectedKey = 'X'.toByte()
        val expectedAsciiString = "Cooking MC's like a pound of bacon"

        val bytes = providedHexString.hexToBytes()

        val trigramFrequencyMap = ngramFrequencyGenerator("src/main/resources/t8.shakespeare.txt", 3)
        val possibleKeys = (0..255).map { it.toByte() }

        val (_, maxScoreKey, maxScoreAsciiString) = likeliestSingleCharXorUsingNgrams(bytes, possibleKeys, trigramFrequencyMap)

        return (maxScoreKey == expectedKey) && (maxScoreAsciiString == expectedAsciiString)
    }
}
