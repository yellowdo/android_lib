package com.yellowdo.library.init

import android.app.Application
import com.yellowdo.library.provider.AbsContentProvider
import com.yellowdo.library.util.Pref

class ContentProviderImpl : AbsContentProvider() {
    override var injectApplicationContext: (Application.() -> Unit)? = {
        Pref.initializeApp(this)
    }
}
