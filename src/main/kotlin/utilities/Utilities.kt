package utilities

import com.google.common.io.BaseEncoding

fun String.toBytes() = BaseEncoding.base16().lowerCase().decode(this)

fun ByteArray.toBase64() = BaseEncoding.base64().encode(this)