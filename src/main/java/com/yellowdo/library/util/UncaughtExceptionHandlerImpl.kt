package com.yellowdo.library.util

import android.os.Environment
import android.util.Log
import com.yellowdo.library.BuildConfig
import com.yellowdo.library.ext.logE
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

object UncaughtExceptionHandlerImpl : Thread.UncaughtExceptionHandler {
    private val TAG = UncaughtExceptionHandlerImpl::class.java.simpleName
    private val uncaughtException: Thread.UncaughtExceptionHandler by lazy { Thread.getDefaultUncaughtExceptionHandler() }
    val notify = mutableMapOf<Class<*>, () -> Unit>()

    fun setDefaultUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        logE("uncaughtException t: $t, e: $e")

        uncaughtExceptionFileSave(e)

        if (notify.isNotEmpty()) for ((_, value) in notify) value()

        // Try everything to make sure this process goes away.
        android.os.Process.killProcess(android.os.Process.myPid())
        System.exit(10)
    }

    fun getStackTrace(th: Throwable): String {
        val result: Writer = StringWriter()
        val printWriter = PrintWriter(result)
        var cause: Throwable? = th
        while (cause != null) {
            cause.printStackTrace(printWriter)
            cause = cause.cause
        }
        val stackTraceAsString = result.toString()
        printWriter.close()
        return stackTraceAsString
    }

    @Suppress("DEPRECATION")
    fun uncaughtExceptionFileSave(th: Throwable) {
        val dirPath = Environment.getExternalStorageDirectory().absolutePath
        val file = File("${dirPath}/winitech")
        if (!file.exists()) file.mkdirs()
        val filePath = StringBuilder()
        filePath.append("${dirPath}/winitech/")
        filePath.append(BuildConfig.APPLICATION_ID + "_")
        filePath.append(SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date(System.currentTimeMillis()).time))
        filePath.append(".txt")
        val saveFile = File(filePath.toString())
        try {
            val fos = FileOutputStream(saveFile)
            fos.write(getStackTrace(th).toByteArray())
            fos.close()
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    inline fun <reified T> add(noinline action: () -> Unit) {
        notify[T::class.java] = action
    }

    inline fun <reified T> remove() {
        notify.remove(T::class.java)
    }

}