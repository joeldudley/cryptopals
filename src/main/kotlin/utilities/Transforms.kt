package utilities

fun transpose(byteArray: ByteArray, rowSize: Int): List<ByteArray> {
    return (0 until rowSize).map { column ->
        val row = mutableListOf<Byte>()
        var idx = column
        while (idx < byteArray.size) {
            row.add(byteArray[idx])
            idx += rowSize
        }
        row.toByteArray()
    }
}

/**
 * Breaks the ByteArray into chunks of size [chunkSize].
 */
fun ByteArray.chunk(chunkSize: Int): List<ByteArray> {
    return toList().chunked(chunkSize).map { it.toByteArray() }
}
