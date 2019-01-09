package challenges.setone

import challenges.Challenge
import utilities.*

/*
The hex encoded string:

    1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736

... has been XOR'd against a single character. Find the key, decrypt the message.

You can do this by hand. But don't: write code to do it for you.

How? Devise some method for "scoring" a piece of English plaintext. Character frequency is a good metric. Evaluate each
output and choose the one with the best score.

Achievement Unlocked

You now have our permission to make "ETAOIN SHRDLU" jokes on Twitter.
*/
object ChallengeThree: Challenge(1, 3) {
    override fun passes(): Boolean {
        val providedCiphertext = "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736".hexToBytes()
        val expectedKey = 'X'.toByte()
        val expectedPlaintext = "Cooking MC's like a pound of bacon".toByteArray()

        val trigramFrequencyMap = ngramFrequencyGenerator("src/main/resources/t8.shakespeare.txt", 3)
        val possibleKeys = (0..255).map { it.toByte() }

        val (_, maxScoreKey, maxScoreBytes) = likeliestSingleCharXorUsingNgrams(providedCiphertext, possibleKeys, trigramFrequencyMap)

        return (maxScoreKey == expectedKey) && (maxScoreBytes.contentEquals(expectedPlaintext))
    }
}
