package challenges.settwo

import challenges.Challenge
import utilities.pad

/*
A block cipher transforms a fixed-sized block (usually 8 or 16 bytes) of plaintext into ciphertext. But we almost never
want to transform a single block; we encrypt irregularly-sized messages.

One way we account for irregularly-sized messages is by padding, creating a plaintext that is an even multiple of the
blocksize. The most popular padding scheme is called PKCS#7.

So: pad any block to a specific block length, by appending the number of bytes of padding to the end of the block. For
instance,

    "YELLOW SUBMARINE"

... padded to 20 bytes would be:

    "YELLOW SUBMARINE\x04\x04\x04\x04"
*/
object ChallengeNine: Challenge(2, 9) {
    override fun passes(): Boolean {
        val providedPlaintext = "YELLOW SUBMARINE".toByteArray()
        val paddedLength = 20
        val expectedBytes = "YELLOW SUBMARINE".toByteArray().pad(4, 4.toByte())

        val paddingLength = paddedLength - providedPlaintext.size
        val paddedBytes = providedPlaintext.pad(paddingLength, paddingLength.toByte())

        return paddedBytes.contentEquals(expectedBytes)
    }
}
