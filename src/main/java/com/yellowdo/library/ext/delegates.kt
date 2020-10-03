package com.yellowdo.library.ext

import androidx.fragment.app.Fragment
import com.yellowdo.library.delegates.FragmentArgumentDelegate
import com.yellowdo.library.delegates.FragmentNullableArgumentDelegate
import kotlin.properties.ReadWriteProperty

fun <T : Any> argument(): ReadWriteProperty<Fragment, T> = FragmentArgumentDelegate()
fun <T : Any> argumentNullable(): ReadWriteProperty<Fragment, T?> = FragmentNullableArgumentDelegate()
