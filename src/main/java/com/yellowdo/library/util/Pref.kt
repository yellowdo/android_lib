package com.yellowdo.library.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ko.go.anavl.lib.ext.toJson
import java.lang.reflect.Type

@SuppressLint("StaticFieldLeak")
object Pref {
    private var context: Context? = null
    private const val PREFERENCE_NAME = "_pref"
    lateinit var prefs: SharedPreferences
        private set
    private lateinit var editor: SharedPreferences.Editor

    @SuppressLint("CommitPrefEdits")
    fun initializeApp(context: Context, name: () -> String = { context.packageName.replace('.', '_') + PREFERENCE_NAME }) {
        this.context = context
        prefs = context.getSharedPreferences(name(), Context.MODE_PRIVATE)
        editor = prefs.edit()
    }

    fun putStringEncryptionExtra(key: String, value: String) {
        context?.run {
            try {
                editor.putString(key, AES256Chiper.chiperAesEncoding(this, value))
            } catch (e: Exception) {
                editor.putString(key, "")
            }
            editor.commit()
        }
    }

    fun getStringEncryptionExtra(key: String, defaultValue: String): String {
        return context?.run {
            try {
                AES256Chiper.chiperAesDecode(this, getStringExtra(key, defaultValue))
            } catch (e: Exception) {
                defaultValue
            }
        } ?: run { defaultValue }
    }

    fun putIntExtra(key: String, value: Int) {
        editor.putInt(key, value)
        editor.commit()
    }

    fun putFloatExtra(key: String, value: Float) {
        editor.putFloat(key, value)
        editor.commit()
    }

    fun putStringExtra(key: String, value: String) {
        editor.putString(key, value)
        editor.commit()
    }

    fun putLongExtra(key: String, value: Long) {
        editor.putLong(key, value)
        editor.commit()
    }

    fun putBooleanExtra(key: String, value: Boolean) {
        editor.putBoolean(key, value)
        editor.commit()
    }

    fun getIntExtra(key: String, defValue: Int = 0): Int {
        return prefs.getInt(key, defValue)
    }

    fun getFloatExtra(key: String, defValue: Float = 0f): Float {
        return prefs.getFloat(key, defValue)
    }

    fun getStringExtra(key: String, defValue: String = ""): String {
        return prefs.getString(key, defValue).takeUnless { it.isNullOrEmpty() } ?: run { "" }
    }

    fun getLongExtra(key: String, defValue: Long = 0): Long {
        return prefs.getLong(key, defValue)
    }

    fun getBooleanExtra(key: String, defValue: Boolean = false): Boolean {
        return prefs.getBoolean(key, defValue)
    }

    @Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
    inline fun <reified T> put(key: String, value: T) {
        when (value) {
            is Boolean -> putBooleanExtra(key, value)
            is Float -> putFloatExtra(key, value)
            is Int -> putIntExtra(key, value)
            is Long -> putLongExtra(key, value)
            is String -> putStringExtra(key, value)
            else -> putStringExtra(key, value.toJson())
        }
    }

    @Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
    inline fun <reified T> get(key: String, defValue: T, type: Type = object : TypeToken<T>() {}.type): T {
        return (when (defValue) {
            is Boolean -> getBooleanExtra(key, defValue)
            is Float -> getFloatExtra(key, defValue)
            is Int -> getIntExtra(key, defValue)
            is Long -> getLongExtra(key, defValue)
            is String -> getStringExtra(key, defValue)
            else -> Gson().fromJson(getStringExtra(key, ""), type)
        }) as T
    }

    fun removePreference(key: String) {
        editor.remove(key).commit()
    }

    fun containCheck(key: String): Boolean {
        return prefs.contains(key)
    }
}
