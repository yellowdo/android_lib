package com.yellowdo.library.ext

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

inline fun CoroutineScope.launchCatching(noinline block: suspend CoroutineScope.() -> Unit, crossinline exceptionHandler: (Throwable) -> Unit) {
    launch(CoroutineExceptionHandler { _, throwable -> exceptionHandler(throwable) }, block = block)
}