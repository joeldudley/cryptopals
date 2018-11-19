package utilities

import com.google.common.base.Charsets.UTF_8
import com.google.common.io.BaseEncoding

fun String.hexToBytes() = BaseEncoding.base16().lowerCase().decode(this)

fun ByteArray.toBase64() = BaseEncoding.base64().encode(this)

fun ByteArray.toHex() = BaseEncoding.base16().encode(this)

fun ByteArray.toAscii() = String(this, UTF_8)
