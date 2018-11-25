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
