package com.yellowdo.library.bindingadapter

import android.content.Context
import android.text.InputFilter
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.databinding.BindingAdapter

@BindingAdapter(value = ["onCheckedChanged"])
fun CheckBox.onCheckedChangeListener(listener: ((CompoundButton, Boolean) -> Unit)? = null) {
    setOnCheckedChangeListener { compoundButton, b -> listener?.invoke(compoundButton, b) }
}

@BindingAdapter(value = ["menuItemClickListener", "longClickListener"])
fun Toolbar.setOnMenuItemClickListener(listener: ((MenuItem, Context) -> Boolean)? = null, longClick: ((View) -> Boolean)? = null) {
    setOnMenuItemClickListener { menuItem -> listener?.invoke(menuItem, context) ?: run { false } }
    setOnLongClickListener { view -> longClick?.invoke(view) ?: run { false } }
}

@BindingAdapter(value = ["setFilters"])
fun EditText.setFilters(filter: Array<InputFilter>) {
    filters = filter
}
