package cryptopals.setone

import cryptopals.Challenge
import utilities.hexToBytes
import utilities.toHex
import utilities.xor

/*
Write a function that takes two equal-length buffers and produces their XOR combination.

If your function works properly, then when you feed it the string:

    1c0111001f010100061a024b53535009181c

... after hex decoding, and when XOR'd against:

    686974207468652062756c6c277320657965

... should produce:

    746865206b696420646f6e277420706c6179
*/
object ChallengeTwo: Challenge(1, 2) {
    override fun passes(): Boolean {
        val providedHexStringOne = "1c0111001f010100061a024b53535009181c"
        val providedHexStringTwo = "686974207468652062756c6c277320657965"
        val expectedXoredHexString = "746865206b696420646f6e277420706c6179"

        val bytesOne = providedHexStringOne.hexToBytes()
        val bytesTwo = providedHexStringTwo.hexToBytes()

        val xoredBytes = bytesOne xor bytesTwo
        val xoredHexString = xoredBytes.toHex()

        return xoredHexString == expectedXoredHexString
    }
}