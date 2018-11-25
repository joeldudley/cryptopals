package utilities

import com.google.common.base.Charsets.UTF_8
import com.google.common.io.BaseEncoding

fun String.hexToBytes(): ByteArray = BaseEncoding.base16().lowerCase().decode(this)

fun String.base64ToBytes(): ByteArray = BaseEncoding.base64().decode(this)

fun ByteArray.toBase64(): String = BaseEncoding.base64().encode(this)

fun ByteArray.toHex(): String = BaseEncoding.base16().lowerCase().encode(this)

fun ByteArray.toAscii() = String(this, UTF_8)

fun ByteArray.toBinary() = joinToString("") { byte ->
    val int = byte.toInt()
    val binaryString = Integer.toBinaryString(int)
    binaryString.padStart(8, '0')
}
