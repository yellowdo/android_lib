package com.yellowdo.library.ext

import com.google.gson.Gson

val gson = Gson()

inline fun <reified T> String.fromJson() = try {
    gson.fromJson(this, T::class.java) as T
} catch (e: Exception) {
    e.printStackTrace()
    null
}

fun <T> T.toJson() = gson.toJson(this)