package com.yellowdo.library.provider

import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.pm.ProviderInfo
import android.database.Cursor
import android.net.Uri


/**
 *  상속 클래스는 manifest에 provider 등록 해야함
 *  ex >
 *  <provider
 *     android:authorities="${applicationId}.authoririesname"
 *     android:name=".BaseContentProviderExtendClassName" />
 **/

open class AbsContentProvider : ContentProvider() {
    protected open var injectApplicationContext: (Application.() -> Unit)? = null

    override fun onCreate(): Boolean {
        injectApplicationContext?.invoke(context as Application)
        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        throw UnsupportedOperationException()
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {
        throw UnsupportedOperationException()
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        throw UnsupportedOperationException()
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        throw UnsupportedOperationException()
    }

    override fun getType(uri: Uri): String? {
        throw UnsupportedOperationException()
    }
}
