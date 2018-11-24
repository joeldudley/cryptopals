package utilities

import com.google.common.base.Charsets.UTF_8
import com.google.common.io.BaseEncoding

fun String.hexToBytes(): ByteArray = BaseEncoding.base16().lowerCase().decode(this)

fun ByteArray.toBase64(): String = BaseEncoding.base64().encode(this)

fun ByteArray.toHex(): String = BaseEncoding.base16().lowerCase().encode(this)

fun ByteArray.toAscii() = String(this, UTF_8)
