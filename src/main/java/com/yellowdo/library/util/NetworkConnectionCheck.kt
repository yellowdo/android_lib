package com.yellowdo.library.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager


object NetworkConnectionCheck {
    var isConnected = false

    // Check Network State
    @SuppressLint("MissingPermission")
    fun networkAvailable(context: Context?): Boolean {
        try {
            val cm = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return isConnected
    }
}
