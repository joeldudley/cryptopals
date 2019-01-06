package cryptopals.setone

import cryptopals.Challenge
import utilities.base64ToBytes
import utilities.hexToBytes
import utilities.toAscii
import utilities.toHex
import java.io.File
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/*
In this file are a bunch of hex-encoded ciphertexts.

One of them has been encrypted with ECB.

Detect it.

Remember that the problem with ECB is that it is stateless and deterministic; the same 16 byte plaintext block will
always produce the same 16 byte ciphertext.
*/
object ChallengeEight : Challenge(1, 8) {
    override fun passes(): Boolean {
        val providedCiphertexts = File("src/main/resources/challengedata/8.txt").readLines()
        val expectedCiphertext = "d880619740a8a19b7840a8a31c810a3d08649af70dc06f4fd5d2d69c744cd283e2dd052f6b641dbf9d11b0348542bb5708649af70dc06f4fd5d2d69c744cd2839475c9dfdbc1d46597949d9c7e82bf5a08649af70dc06f4fd5d2d69c744cd28397a93eab8d6aecd566489154789a6b0308649af70dc06f4fd5d2d69c744cd283d403180c98c8f6db1f2a3f9c4040deb0ab51b29933f2c123c58386b06fba186a"

        val ciphertextsAsBytes = providedCiphertexts.map { it.hexToBytes() }

        val ecbBlockSize = 16
        val totalBlocks = ciphertextsAsBytes.first().size / ecbBlockSize

        var ecbEncryptedCiphertextBytes = ByteArray(0)
        var maxRepeatingCiphertexts = 0
        ciphertextsAsBytes.forEach { bytes ->
            var repeatingCiphertexts = 0
            // TODO: Using a set isn't very efficient due to the way byte array equality works. Working around it for
            // TODO: now using toString().
            val dict = mutableSetOf<String>()

            (0 until totalBlocks).forEach { blockIdx ->
                val block = bytes.slice((blockIdx * ecbBlockSize) until (blockIdx + 1) * ecbBlockSize).toString()
                if (block in dict) repeatingCiphertexts += 1
                else dict.add(block)
            }

            if (repeatingCiphertexts > maxRepeatingCiphertexts) {
                maxRepeatingCiphertexts = repeatingCiphertexts
                ecbEncryptedCiphertextBytes = bytes
            }
        }

        return ecbEncryptedCiphertextBytes.toHex() == expectedCiphertext
    }
}
