package com.yellowdo.library.ext

import android.content.Context
import android.content.DialogInterface
import android.text.method.ScrollingMovementMethod
import android.util.TypedValue
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.appcompat.R
import androidx.appcompat.app.AlertDialog

fun Context.showAlertDialog(
    @StyleRes themeResId: Int = TypedValue().let {
        theme.resolveAttribute(R.attr.alertDialogTheme, it, true)
        it.resourceId
    },
    title: String = "",
    message: String,
    messageView: TextView.() -> Unit = {},
    ok: String = "ok",
    cancel: String = "cancel",
    positiveListener: ((DialogInterface, Int) -> Unit)? = null,
    negativeListener: ((DialogInterface, Int) -> Unit)? = null
) {
    AlertDialog.Builder(this, themeResId).apply {
        if (title.isNotEmpty()) setTitle(title)
        if (message.isNotEmpty()) setMessage(message)
        setCancelable(false)
        positiveListener?.let {
            setPositiveButton(ok) { dialog, which ->
                it.invoke(dialog, which)
                dialog.dismiss()
            }
        }
        setNegativeButton(cancel) { dialog, which ->
            negativeListener?.invoke(dialog, which)
            dialog.dismiss()
        }
    }.show().apply {
        (findViewById<TextView>(android.R.id.message))?.apply {
            movementMethod = ScrollingMovementMethod()
        }?.apply(messageView)
    }
}


fun Context.showAlertDialog(
    @StyleRes themeResId: Int = TypedValue().let {
        theme.resolveAttribute(R.attr.alertDialogTheme, it, true)
        it.resourceId
    },
    @StringRes title: Int = 0,
    @StringRes message: Int = 0,
    messageView: TextView.() -> Unit = {},
    @StringRes ok: Int = 0,
    @StringRes cancel: Int = 0,
    positiveListener: ((DialogInterface, Int) -> Unit)? = null,
    negativeListener: ((DialogInterface, Int) -> Unit)? = null
) {
    val t = if (title != 0) getString(title) else ""
    val m = if (message != 0) getString(message) else ""
    val o = if (ok != 0) getString(ok) else ""
    val c = if (cancel != 0) getString(cancel) else ""
    showAlertDialog(themeResId = themeResId, title = t, message = m, messageView = messageView, ok = o, cancel = c, positiveListener = positiveListener, negativeListener = negativeListener)

}

fun Context.showAlertDialogApply(block: AlertDialog.Builder.() -> Unit) {
    AlertDialog.Builder(this).apply(block).show()
}

fun Context.alertDialogShowWithEditText(
    ok: String?,
    cancel: String?,
    text: String,
    action: (String) -> Unit = {}
) {
    val input = EditText(this).apply {
        layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT).apply { setMargins(16, 12, 16, 12) }
        imeOptions = EditorInfo.IME_ACTION_DONE
        isSingleLine = true
        setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> false
                else -> true
            }
        }
        setText(text)
        setSelection(text.length)
        requestFocus()
    }

    AlertDialog.Builder(this).apply {
        setView(input)
        setPositiveButton(ok) { d, _ ->
            d.dismiss()
            action(input.text.toString())
        }
        setNegativeButton(cancel) { d, _ -> d.dismiss() }
        setCancelable(false)

    }.show()

}