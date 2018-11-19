package utilities

import java.io.File

fun scoreAsciiUsingNgrams(asciiString: String, ngramSize: Int, trigramFrequencyMap: Map<String, Int>): Int {
    var score = 0

    val upperBound = asciiString.length - ngramSize

    for (i in 0..upperBound) {
        val ngram = asciiString.substring(i, i + ngramSize)
        score += trigramFrequencyMap.getOrDefault(ngram, 0)
    }

    return score
}

fun ngramFrequencyGenerator(filepath: String, ngram_size: Int): Map<String, Int> {
    val ngramFrequencyDict = mutableMapOf<String, Int>()

    val file = File(filepath)
    file.inputStream().use { inputStream ->
        // We chomp three bytes of text at a time (i.e. bytes[0..3], then bytes[3..6], etc.).
        // An alternative would be to move through the bytes one-by-one (i.e. bytes[0..3], then bytes[1..4], etc.).
        val bytes = ByteArray(ngram_size)
        while (inputStream.read(bytes) != -1) {
            val ascii = bytes.toAscii()
            ngramFrequencyDict[ascii] = ngramFrequencyDict.getOrDefault(ascii, 0) + 1
        }
    }

    return ngramFrequencyDict
}