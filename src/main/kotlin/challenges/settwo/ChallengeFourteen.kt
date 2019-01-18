package challenges.settwo

import challenges.Challenge
import ciphers.toyciphers.ECBUnknownKeyAndPrefixAndSuffixCipher
import ciphers.toyciphers.ECBUnknownKeyAndSuffixCipher
import utilities.retrieveUnknownSuffixEcb

/*
Take your oracle function from #12. Now generate a random count of random bytes and prepend this string to every plaintext. You are now doing:

    AES-128-ECB(random-prefix || attacker-controlled || target-bytes, random-key)

Same goal: decrypt the target-bytes.

Stop and think for a second.

What's harder than challenge #12 about doing this? How would you overcome that obstacle? The hint is: you're using all the tools you already have; no crazy math is required.

Think "STIMULUS" and "RESPONSE".
*/
object ChallengeFourteen : Challenge(2, 14) {
    override fun passes(): Boolean {
        val expectedSuffix = ("Rollin' in my 5.0\nWith my rag-top down so my hair can blow\nThe girlies on stan" +
                "dby waving just to say hi\nDid you stop? No, I just drove by\n").toByteArray()

        val cipher = ECBUnknownKeyAndPrefixAndSuffixCipher()
        val suffix = retrieveUnknownSuffixEcb(cipher)

        return suffix.contentEquals(expectedSuffix)
    }
}

fun main(args: Array<String>) {
    ChallengeFourteen.run()
}
