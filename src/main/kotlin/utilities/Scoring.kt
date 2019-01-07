package utilities

import java.io.File

fun ngramFrequencyGenerator(filepath: String, ngramSize: Int): Map<String, Int> {
    val ngramFrequencyDict = mutableMapOf<String, Int>()

    File(filepath).bufferedReader().use { inputStream ->
        // We chomp three chars of text at a time (i.e. chars[0..3], then chars[3..6], etc.).
        // An alternative would be to move through the chars one-by-one (i.e. chars[0..3], then chars[1..4], etc.).
        val chars = CharArray(ngramSize)
        while (inputStream.read(chars) != -1) {
            val ascii = String(chars)
            ngramFrequencyDict[ascii] = ngramFrequencyDict.getOrDefault(ascii, 0) + 1
        }
    }

    return ngramFrequencyDict
}

fun scoreAsciiUsingNgrams(asciiString: String, ngramFrequencyMap: Map<String, Int>): Int {
    val ngramSize = ngramFrequencyMap.keys.iterator().next().length

    val upperBound = asciiString.length - ngramSize

    val asciiSubstrings = (0..upperBound).map { asciiString.substring(it, it + ngramSize) }

    return asciiSubstrings.sumBy { ngramFrequencyMap.getOrDefault(it, 0) }
}

fun likeliestSingleCharXorUsingNgrams(bytes: ByteArray, possibleKeys: List<Byte>, ngramFrequencyMap: Map<String, Int>): Triple<Int, Byte, String> {
    var maxScore = 0
    var maxScoreKey: Byte = 0

    possibleKeys.forEach { possibleKey ->
        val xoredBytes = (bytes xor possibleKey)
        val xoredAsciiString = xoredBytes.toAscii()
        val score = scoreAsciiUsingNgrams(xoredAsciiString, ngramFrequencyMap)
        if (score > maxScore) {
            maxScore = score
            maxScoreKey = possibleKey
        }
    }

    val maxScoreAsciiString = (bytes xor maxScoreKey).toAscii()

    return Triple(maxScore, maxScoreKey, maxScoreAsciiString)
}

fun hammingDistance(bytesOne: ByteArray, bytesTwo: ByteArray): Int {
    val oneBinary = bytesOne.toBinary()
    val twoBinary = bytesTwo.toBinary()

    var hammingDistance = 0
    oneBinary.zip(twoBinary).forEach { (bitOne, bitTwo) ->
        if (bitOne != bitTwo) hammingDistance++
    }

    return hammingDistance
}
