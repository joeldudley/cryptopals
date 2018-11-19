package setone

import utilities.hexToBytes
import utilities.toHex
import utilities.xor

fun main(args: Array<String>) {
    val providedHexStringOne = "1c0111001f010100061a024b53535009181c"
    val providedHexStringTwo = "686974207468652062756c6c277320657965"
    val expectedXoredHexString = "746865206b696420646f6e277420706c6179"

    val bytesOne = providedHexStringOne.hexToBytes()
    val bytesTwo = providedHexStringTwo.hexToBytes()

    val xoredBytes = bytesOne xor bytesTwo
    val xoredHexString = xoredBytes.toHex()

    assert(xoredHexString == expectedXoredHexString)
}