package com.yellowdo.library.ext

import android.app.Service
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun Service.toastShow(msg: Any?, duration: Int = Toast.LENGTH_SHORT, unit: (() -> Unit)? = null) {
    when (msg) {
        is String -> msg
        is Int -> getString(msg)
        else -> null
    }?.let { CoroutineScope(Dispatchers.Main).launch { Toast.makeText(applicationContext, it, duration).show() } }
    unit?.invoke()
}
