package com.yellowdo.library.ext

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

val String.hexAsByteArray inline get() = this.chunked(2).map { it.toUpperCase().toInt(16).toByte() }.toByteArray()

@SuppressLint("SimpleDateFormat")
fun nowSecond(): Int = try {
    SimpleDateFormat("ss").run { format(Date(System.currentTimeMillis())).toInt() }
} catch (e: Exception) {
    0
}

@SuppressLint("SimpleDateFormat")
fun nowKr(): String = SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초").run { format(Date(System.currentTimeMillis())) } ?: run { "" }

@SuppressLint("SimpleDateFormat")
fun nowKrDate(): String = SimpleDateFormat("yyyy년 MM월 dd일").run { format(Date(System.currentTimeMillis())) } ?: run { "" }

@SuppressLint("SimpleDateFormat")
fun nowKrTime(): String = SimpleDateFormat("HH시 mm분 ss초").run { format(Date(System.currentTimeMillis())) } ?: run { "" }

@SuppressLint("SimpleDateFormat")
fun pairDateKr(firstPattern: String = "yyyyMMdd", secondPattern: String = "yyyy년 MM월 dd일", addTime: String = "", block: Calendar.() -> Unit = {}): Pair<String, String> = Date(
    Calendar.getInstance().apply(block).timeInMillis
).let { SimpleDateFormat(firstPattern).format(it) + addTime to SimpleDateFormat(secondPattern).format(it) }

@SuppressLint("SimpleDateFormat")
fun String.formatDateKr(): String {
    if (isNullOrBlank()) return ""
    val format = SimpleDateFormat("yyyyMMdd")
    return SimpleDateFormat("yyyy년 MM월 dd일").run { format(format.parse(this@formatDateKr)) } ?: run { "" }
}

fun String.yyyyMMddHHmmssToDateKr() = formatDateTime("yyyyMMddHHmmss", "yyyy년 MM월 dd일")

fun String.yyyyMMddHHmmssToTimeKr() = formatDateTime("yyyyMMddHHmmss", "HH시 mm분 ss초")

fun String.yyyyMMddHHmmssToKr() = formatDateTime("yyyyMMddHHmmss", "yy년MM월dd일 HH시mm분ss초")

@SuppressLint("SimpleDateFormat")
fun String.formatDateTime(intoFormat: String = "yyyyMMddHHmmss", outFomat: String = "yyyy-MM-dd HH:mm:ss"): String {
    if (isNullOrBlank()) return ""
    val format = SimpleDateFormat(intoFormat)
    return SimpleDateFormat(outFomat).run { format(format.parse(this@formatDateTime)) } ?: run { "" }
}