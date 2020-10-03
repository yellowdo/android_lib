package com.yellowdo.library.bindingadapter

import android.annotation.SuppressLint
import android.view.KeyEvent
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.isNotEmpty
import androidx.databinding.BindingAdapter

@BindingAdapter(value = ["setKeyListener"])
fun WebView.setOnKeyListener(listener: ((WebView, Int, KeyEvent?) -> Boolean)? = null) {
    setOnKeyListener { _, keyCode, event -> listener?.invoke(this, keyCode, event) ?: run { false } }
}


@SuppressLint("SetJavaScriptEnabled")
@BindingAdapter(value = ["setWebViewClient"])
fun WebView.setWebViewClient(client: WebViewClient) {
    //clearCache(true)
    settings.javaScriptEnabled = true
    //settings.setAppCacheEnabled(false)
    //settings.setSupportZoom(true)
    //settings.builtInZoomControls = true
    settings.javaScriptCanOpenWindowsAutomatically = true
    //settings.loadWithOverviewMode = true
    //settings.useWideViewPort = false

    //isHorizontalScrollBarEnabled = false //가로 스크롤
    //isVerticalScrollBarEnabled = false   //세로 스크롤
    webViewClient = client
}

@BindingAdapter(value = ["setWebChromeClient"])
fun WebView.setWebChromeClient(client: WebChromeClient) {
    webChromeClient = client
}

@BindingAdapter(value = ["loadUrl"])
fun WebView.loadUrlWebView(url: String?) {
    url?.takeIf { isNotEmpty() }?.run { loadUrl(url) }
}
