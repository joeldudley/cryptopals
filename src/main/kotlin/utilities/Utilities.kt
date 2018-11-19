package utilities

import com.google.common.io.BaseEncoding
import kotlin.experimental.xor

fun String.toBytes() = BaseEncoding.base16().lowerCase().decode(this)

fun ByteArray.toBase64() = BaseEncoding.base64().encode(this)

fun ByteArray.toHex() = BaseEncoding.base16().lowerCase().encode(this)

infix fun ByteArray.xor(otherBytes: ByteArray) = zip(otherBytes).map { it.first xor it.second }.toByteArray()