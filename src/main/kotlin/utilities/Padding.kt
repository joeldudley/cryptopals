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

fun ByteArray.stripPadding() : ByteArray {
    val paddingStartIndex = indexOf(0.toByte())
    return if (paddingStartIndex == -1) {
        this
    } else {
        sliceArray(0 until paddingStartIndex)
    }
}
