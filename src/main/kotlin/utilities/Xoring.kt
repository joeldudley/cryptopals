package utilities

import kotlin.experimental.xor

infix fun ByteArray.xor(otherBytes: ByteArray) = zip(otherBytes).map { it.first xor it.second }.toByteArray()

infix fun ByteArray.xor(byte: Byte) = map { it xor byte }.toByteArray()
