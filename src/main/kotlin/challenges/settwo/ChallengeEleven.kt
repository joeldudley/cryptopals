package challenges.settwo

import challenges.Challenge
import utilities.chunk
import utilities.encryptWithAESInCBCOrECBWithRandomKeyAndRandomPadding

/*
Now that you have ECB and CBC working:

Write a function to generate a random AES key; that's just 16 random bytes.

Write a function that encrypts data under an unknown key --- that is, a function that generates a random key and
encrypts under it.

The function should look like:

    encryption_oracle(your-input)
    => [MEANINGLESS JIBBER JABBER]

Under the hood, have the function append 5-10 bytes (count chosen randomly) before the plaintext and 5-10 bytes after
the plaintext.

Now, have the function choose to encrypt under ECB 1/2 the time, and under CBC the other half (just use random IVs each
time for CBC). Use rand(2) to decide which to use.

Detect the block cipher mode the function is using each time. You should end up with a piece of code that, pointed at a
block box that might be encrypting ECB or CBC, tells you which one is happening.
*/
object ChallengeEleven : Challenge(2, 11) {
    override fun passes(): Boolean {
        val plaintext = ByteArray(320) { 0.toByte() }
        val ciphertext = encryptWithAESInCBCOrECBWithRandomKeyAndRandomPadding(plaintext)

        val ciphertextBlocks = ciphertext.chunk(16)
        // We skip over the block containing the IV.
        if (ciphertextBlocks[1].contentEquals(ciphertextBlocks[2])
                && ciphertextBlocks[2].contentEquals(ciphertextBlocks[3])) {
            // Is using ECB mode, since identical plaintext blocks have the same ciphertext.
        } else {
            // Is using CBC mode, since identical plaintext blocks don't have the same ciphertext.
        }

        return true
    }
}
