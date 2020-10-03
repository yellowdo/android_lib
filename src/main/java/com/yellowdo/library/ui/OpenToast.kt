package com.yellowdo.library.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.yellowdo.library.R

@SuppressLint("StaticFieldLeak")
object OpenToast {
    private var context: Context? = null
    private var toast: Toast? = null
    private var view: View? = null
    private var textView: TextView? = null

    @SuppressLint("ShowToast", "InflateParams")
    fun getInstance(context: Context?): OpenToast {
        OpenToast.context = context
        toast?.cancel()
        toast = Toast.makeText(context, "", Toast.LENGTH_LONG)
        context?.apply {
            val inflater = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            if (view == null) view = inflater.inflate(R.layout.toast, null, false)
            if (textView == null) textView = view?.findViewById(R.id.toast_text)
        }

        return this
    }

    fun show(message: String, duration: Int = Toast.LENGTH_LONG) {
        toast?.run {
            textView?.text = message
            OpenToast.view?.run { view = this }
            setDuration(duration)
            show()
        }
    }
}
