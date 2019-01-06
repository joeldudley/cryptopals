package cryptopals.setone

import cryptopals.Challenge
import utilities.*
import java.io.File

/*
It is officially on, now.

This challenge isn't conceptually hard, but it involves actual error-prone coding. The other challenges in this set are there to bring you up to speed. This one is there to qualify you. If you can do this one, you're probably just fine up to Set 6.

There's a file here. It's been base64'd after being encrypted with repeating-key XOR.

Decrypt it.

Here's how:

1. Let KEYSIZE be the guessed length of the key; try values from 2 to (say) 40.

2. Write a function to compute the edit distance/Hamming distance between two strings. The Hamming distance is just the
number of differing bits. The distance between:

    this is a test

and

    wokka wokka!!!

is 37. Make sure your code agrees before you proceed.

3. For each KEYSIZE, take the first KEYSIZE worth of bytes, and the second KEYSIZE worth of bytes, and find the edit
distance between them. Normalize this result by dividing by KEYSIZE.

4. The KEYSIZE with the smallest normalized edit distance is probably the key. You could proceed perhaps with the
smallest 2-3 KEYSIZE values. Or take 4 KEYSIZE blocks instead of 2 and average the distances.

5. Now that you probably know the KEYSIZE: break the ciphertext into blocks of KEYSIZE length.

6. Now transpose the blocks: make a block that is the first byte of every block, and a block that is the second byte of
every block, and so on.

7. Solve each block as if it was single-character XOR. You already have code to do this.

8. For each block, the single-byte XOR key that produces the best looking histogram is the repeating-key XOR key byte
for that block. Put them together and you have the key.

This code is going to turn out to be surprisingly useful later on. Breaking repeating-key XOR ("Vigenere")
statistically is obviously an academic exercise, a "Crypto 101" thing. But more people "know how" to break it than can
actually break it, and a similar technique breaks something much more important.

No, that's not a mistake.

We get more tech support questions for this challenge than any of the other ones. We promise, there aren't any blatant
errors in this text. In particular: the "wokka wokka!!!" edit distance really is 37.
*/
object ChallengeSix : Challenge(1, 6) {
    override fun passes(): Boolean {
        val providedEncryptedBase64String = File("src/main/resources/challengedata/6.txt").readText().filter { it != '\n' }
        val providedMaxPossibleKeySize = 40
        val encryptedBytes = providedEncryptedBase64String.base64ToBytes()
        val expectedAsciiString = "I'm back and I'm ringin' the bell \nA rockin' on the mike while the fly girls " +
                "yell \nIn ecstasy in the back of me \nWell that's my DJ Deshay cuttin' all them Z's \nHittin' hard " +
                "and the girlies goin' crazy \nVanilla's on the mike, man I'm not lazy. \n\nI'm lettin' my drug " +
                "kick in \nIt controls my mouth and I begin \nTo just let it flow, let my concepts go \nMy posse's " +
                "to the side yellin', Go Vanilla Go! \n\nSmooth 'cause that's the way I will be \nAnd if you don't " +
                "give a damn, then \nWhy you starin' at me \nSo get off 'cause I control the stage \nThere's no " +
                "dissin' allowed \nI'm in my own phase \nThe girlies sa y they love me and that is ok \nAnd I can " +
                "dance better than any kid n' play \n\nStage 2 -- Yea the one ya' wanna listen to \nIt's off my " +
                "head so let the beat play through \nSo I can funk it up and make it sound good \n1-2-3 Yo -- Knock " +
                "on some wood \nFor good luck, I like my rhymes atrocious \nSupercalafragilisticexpialidocious \n" +
                "I'm an effect and that you can bet \nI can take a fly girl and make her wet. \n\nI'm like Samson " +
                "-- Samson to Delilah \nThere's no denyin', You can try to hang \nBut you'll keep tryin' to get my " +
                "style \nOver and over, practice makes perfect \nBut not if you're a loafer. \n\nYou'll get " +
                "nowhere, no place, no time, no girls \nSoon -- Oh my God, homebody, you probably eat \nSpaghetti " +
                "with a spoon! Come on and say it! \n\nVIP. Vanilla Ice yep, yep, I'm comin' hard like a rhino \n" +
                "Intoxicating so you stagger like a wino \nSo punks stop trying and girl stop cryin' \nVanilla Ice " +
                "is sellin' and you people are buyin' \n'Cause why the freaks are jockin' like Crazy Glue \nMovin' " +
                "and groovin' trying to sing along \nAll through the ghetto groovin' this here song \nNow you're " +
                "amazed by the VIP posse. \n\nSteppin' so hard like a German Nazi \nStartled by the bases hittin' " +
                "ground \nThere's no trippin' on mine, I'm just gettin' down \nSparkamatic, I'm hangin' tight like " +
                "a fanatic \nYou trapped me once and I thought that \nYou might have it \nSo step down and lend me " +
                "your ear \n'89 in my time! You, '90 is my year. \n\nYou're weakenin' fast, YO! and I can tell it \n" +
                "Your body's gettin' hot, so, so I can smell it \nSo don't be mad and don't be sad \n'Cause the " +
                "lyrics belong to ICE, You can call me Dad \nYou're pitchin' a fit, so step back and endure \nLet " +
                "the witch doctor, Ice, do the dance to cure \nSo come up close and don't be square \nYou wanna " +
                "battle me -- Anytime, anywhere \n\nYou thought that I was weak, Boy, you're dead wrong \nSo come " +
                "on, everybody and sing this song \n\nSay -- Play that funky music Say, go white boy, go white boy " +
                "go \nplay that funky music Go white boy, go white boy, go \nLay down and boogie and play that " +
                "funky music till you die. \n\nPlay that funky music Come on, Come on, let me hear \nPlay that " +
                "funky music white boy you say it, say it \nPlay that funky music A little louder now \nPlay that " +
                "funky music, white boy Come on, Come on, Come on \nPlay that funky music \n"

        val keySize = findRepeatingKeyXorKeySize(encryptedBytes, providedMaxPossibleKeySize)

        val blocks = transpose(encryptedBytes, keySize)

        val trigramFrequencyMap = ngramFrequencyGenerator("src/main/resources/t8.shakespeare.txt", 1)
        val possibleKeys = (0..255).map { it.toByte() }

        val keys = blocks.map { block ->
            val (_, maxScoreKey, _) = likeliestSingleCharXorUsingNgrams(block, possibleKeys, trigramFrequencyMap)
            maxScoreKey
        }.toByteArray()

        val decryptedAscii = encryptWithRepeatingKeyXor(encryptedBytes, keys).toAscii()

        return decryptedAscii == expectedAsciiString
    }
}
