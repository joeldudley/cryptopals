package setone

import utilities.toBase64
import utilities.toBytes

fun main(args: Array<String>) {
    val providedHexString = "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d"
    val expectedBase64String = "SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t"

    val bytes = providedHexString.toBytes()
    val base64String = bytes.toBase64()

    assert(base64String == expectedBase64String)
}