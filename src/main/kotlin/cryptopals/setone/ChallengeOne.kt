package cryptopals.setone

import cryptopals.Challenge
import utilities.hexToBytes
import utilities.toBase64

object ChallengeOne: Challenge(1, 1) {
    override fun passes(): Boolean {
        val providedHexString = "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d"
        val expectedBase64String = "SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t"

        val bytes = providedHexString.hexToBytes()
        val base64String = bytes.toBase64()

        return base64String == expectedBase64String
    }
}