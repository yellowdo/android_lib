package com.yellowdo.library.ext

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

fun Application.isDebuggable(): Boolean {
    return try {
        0 != this.packageManager.getApplicationInfo(this.packageName, 0).flags and ApplicationInfo.FLAG_DEBUGGABLE
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}
