package setone

import utilities.*

fun main(args: Array<String>) {
    val providedHexString = "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736"
    val expectedAsciiString = "Cooking MC's like a pound of bacon"

    val bytes = providedHexString.hexToBytes()

    val trigramFrequencyMap = ngramFrequencyGenerator("src/main/resources/t8.shakespeare.txt", 3)

    val asciiBytes = (0..127).map { it.toByte() }

    var maxScoreByte: Byte = 0
    var maxScore = 0
    asciiBytes.forEach { asciiByte ->
        val xoredBytes = (bytes xor asciiByte)
        val xoredAsciiString = xoredBytes.toAscii()
        val score = scoreAsciiUsingNgrams(xoredAsciiString, 3, trigramFrequencyMap)
        if (score > maxScore) {
            maxScoreByte = asciiByte
            maxScore = score
        }
    }

    val asciiString = (bytes xor maxScoreByte).toAscii()

    assert(asciiString == expectedAsciiString)
}
