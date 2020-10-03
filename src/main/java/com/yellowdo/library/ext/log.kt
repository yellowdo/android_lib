package com.yellowdo.library.ext

import android.util.Log
import com.yellowdo.library.BuildConfig

var LOG_TAG = "log_extension"

fun logV(msg: String?, tag: String = LOG_TAG, showCallStack: Boolean = false) {
    msg?.takeIf { it.isNotEmpty() and BuildConfig.DEBUG }?.run {
        when (showCallStack) {
            true -> Log.v(tag, buildLogMsg(this), Throwable())
            false -> Log.v(tag, buildLogMsg(this))
        }
    }
}

fun logD(msg: String?, tag: String = LOG_TAG, showCallStack: Boolean = false) {
    msg?.takeIf { it.isNotEmpty() and BuildConfig.DEBUG }?.run {
        when (showCallStack) {
            true -> Log.d(tag, buildLogMsg(this), Throwable())
            false -> Log.d(tag, buildLogMsg(this))
        }
    }
}

fun logI(msg: String?, tag: String = LOG_TAG, showCallStack: Boolean = false) {
    msg?.takeIf { it.isNotEmpty() and BuildConfig.DEBUG }?.run {
        when (showCallStack) {
            true -> Log.i(tag, buildLogMsg(this), Throwable())
            false -> Log.i(tag, buildLogMsg(this))
        }
    }
}

fun logW(msg: String?, tag: String = LOG_TAG, showCallStack: Boolean = false) {
    msg?.takeIf { it.isNotEmpty() and BuildConfig.DEBUG }?.run {
        when (showCallStack) {
            true -> Log.w(tag, buildLogMsg(this), Throwable())
            false -> Log.w(tag, buildLogMsg(this))
        }
    }
}

fun logE(msg: String?, tag: String = LOG_TAG, showCallStack: Boolean = false) {
    msg?.takeIf { it.isNotEmpty() and BuildConfig.DEBUG }?.run {
        when (showCallStack) {
            true -> Log.e(tag, buildLogMsg(this), Throwable())
            false -> Log.e(tag, buildLogMsg(this))
        }
    }
}

fun buildLogMsg(message: String): String {
    val ste = Thread.currentThread().stackTrace[5]
    return StringBuilder().apply {
        append("[ (")
        append(ste.fileName)
        append(":")
        append(ste.lineNumber)
        append(")::")
        append(ste.methodName)
        append(" ] ")
        append(message)
    }.toString()
}
