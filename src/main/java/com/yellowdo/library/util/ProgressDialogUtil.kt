package com.yellowdo.library.util

import android.app.ProgressDialog
import android.content.Context
import android.view.WindowManager

object ProgressDialogUtil {
    private var progressDialog: ProgressDialog? = null

    fun show(context: Context, block: ProgressDialog.() -> Unit = {}) {
        progressDialog = ProgressDialog(context).apply {
            setMessage("Loading...")
            setCancelable(true)
            //setOnKeyListener(null)
            window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }.apply(block)
        progressDialog?.show()

    }

    fun dismiss() {
        progressDialog?.run { if (isShowing) dismiss() }
    }
}