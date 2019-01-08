package utilities

fun ByteArray.pad(length: Int, value: Byte) = this + ByteArray(length) { value }

fun ByteArray.padToMultipleOf(length: Int): ByteArray {
    return if (size % length == 0) {
        this
    } else {
        val paddingLength = length - (size % length)
        pad(paddingLength, 0.toByte())
    }
}
