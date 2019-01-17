package challenges.settwo

import challenges.Challenge
import ciphers.Cipher
import ciphers.toyciphers.ECBUnknownKeyAndSuffixCipher
import utilities.determineBlocksize
import utilities.toAscii

/*
Copy your oracle function to a new function that encrypts buffers under ECB mode using a consistent but unknown key
(for instance, assign a single random key, once, to a global variable).

Now take that same function and have it append to the plaintext, BEFORE ENCRYPTING, the following string:

    Um9sbGluJyBpbiBteSA1LjAKV2l0aCBteSByYWctdG9wIGRvd24gc28gbXkg
    aGFpciBjYW4gYmxvdwpUaGUgZ2lybGllcyBvbiBzdGFuZGJ5IHdhdmluZyBq
    dXN0IHRvIHNheSBoaQpEaWQgeW91IHN0b3A/IE5vLCBJIGp1c3QgZHJvdmUg
    YnkK

Spoiler alert.

Do not decode this string now. Don't do it.

Base64 decode the string before appending it. Do not base64 decode the string by hand; make your code do it. The point
is that you don't know its contents.

What you have now is a function that produces:

    AES-128-ECB(your-string || unknown-string, random-key)

It turns out: you can decrypt "unknown-string" with repeated calls to the oracle function!

Here's roughly how:

1. Feed identical bytes of your-string to the function 1 at a time --- start with 1 byte ("A"), then "AA", then "AAA"
and so on. Discover the block size of the cipher. You know it, but do this step anyway.

2. Detect that the function is using ECB. You already know, but do this step anyways.

3. Knowing the block size, craft an input block that is exactly 1 byte short (for instance, if the block size is 8
bytes, make "AAAAAAA"). Think about what the oracle function is going to put in that last byte position.

4. Make a dictionary of every possible last byte by feeding different strings to the oracle; for instance, "AAAAAAAA",
"AAAAAAAB", "AAAAAAAC", remembering the first block of each invocation.

5. Match the output of the one-byte-short input to one of the entries in your dictionary. You've now discovered the
first byte of unknown-string.

6. Repeat for the next byte.

Congratulations.

This is the first challenge we've given you whose solution will break real crypto. Lots of people know that when you
encrypt something in ECB mode, you can see penguins through it. Not so many of them can decrypt the contents of those
ciphertexts, and now you can. If our experience is any guideline, this attack will get you code execution in security
tests about once a year.
*/
object ChallengeTwelve : Challenge(2, 12) {
    override fun passes(): Boolean {
        val expectedSuffix = ("Rollin' in my 5.0\nWith my rag-top down so my hair can blow\nThe girlies on stan" +
                "dby waving just to say hi\nDid you stop? No, I just drove by\n").toByteArray()

        val cipher = ECBUnknownKeyAndSuffixCipher()
        val suffix = retrieveUnknownSuffix(cipher)

        return suffix.contentEquals(expectedSuffix)
    }
}

fun retrieveUnknownSuffix(cipher: Cipher): ByteArray {
    val blocksize = determineBlocksize(cipher)

    var decipheredSuffix = ByteArray(0)
    val allBytes = (0..255).map { it.toByte() }

    var suffixByteIndex = 0
    while (true) {
        val plaintextBlocksToGenerate = suffixByteIndex / blocksize + 1
        val plaintextBytesToGenerate = plaintextBlocksToGenerate * blocksize
        val nullBytesToGenerate = plaintextBytesToGenerate - decipheredSuffix.size - 1
        val nullBytes = ByteArray(nullBytesToGenerate) { 0.toByte() }
        val nullBytesAndDecipheredBytes = nullBytes + decipheredSuffix

        val ciphertextOfShortInput = cipher.encrypt(nullBytes)
        // If the resulting ciphertext is shorter than expected, this indicates that the suffix has finished.
        if (ciphertextOfShortInput.size < plaintextBytesToGenerate) break
        val relevantCiphertextOfShortInput = ciphertextOfShortInput.sliceArray(0 until plaintextBytesToGenerate)

        val ciphertextOfEachAdditionalByteMap = allBytes.map { byte ->
            val plaintext = nullBytesAndDecipheredBytes + byte
            val ciphertext = cipher.encrypt(plaintext)
            val relevantCiphertextBytes = ciphertext.sliceArray(0 until plaintextBytesToGenerate)
            relevantCiphertextBytes.toAscii() to byte
        }.toMap()

        val decipheredByte = ciphertextOfEachAdditionalByteMap[relevantCiphertextOfShortInput.toAscii()]
        // Indicates we've hit the padding.
        if (decipheredByte == 0.toByte()) break

        decipheredSuffix += decipheredByte!!
        suffixByteIndex++
    }

    return decipheredSuffix
}
