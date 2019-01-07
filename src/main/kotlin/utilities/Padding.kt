package utilities

fun ByteArray.pad(length: Int, value: Byte) = this + ByteArray(length) { value }
