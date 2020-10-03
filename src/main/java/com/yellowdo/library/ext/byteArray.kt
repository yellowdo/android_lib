package com.yellowdo.library.ext

@ExperimentalUnsignedTypes // just to make it clear that the experimental unsigned types are used
fun ByteArray.toHexString() = asUByteArray().joinToString("") { it.toString(16).padStart(2, '0') }

val ByteArray.asHexLower inline get() = this.joinToString(separator = "") { String.format("%02x", (it.toInt() and 0xFF)) }

val ByteArray.asHexUpper inline get() = this.joinToString(separator = "") { String.format("%02X", (it.toInt() and 0xFF)) }
