package com.yellowdo.library.ext

import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

fun <T : Preference> PreferenceFragmentCompat.preference(key: CharSequence): T? {
    return findPreference(key)
}
