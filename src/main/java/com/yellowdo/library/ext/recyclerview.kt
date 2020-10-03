package com.yellowdo.library.ext

import android.annotation.SuppressLint
import androidx.recyclerview.selection.SelectionTracker

fun <T> selectionObserver(block: SelectionObserverImpl<T>.() -> Unit) = SelectionObserverImpl<T>().apply(block)

class SelectionObserverImpl<T> : SelectionTracker.SelectionObserver<T>() {
    var selectionRefresh = {}
    var selectionRestored = {}
    var itemStateChanged = { _: T, _: Boolean -> }
    var selectionChanged = {}
    var selectionCleared = {}

    override fun onSelectionRefresh() {
        super.onSelectionRefresh()
        selectionRefresh()
    }

    override fun onSelectionRestored() {
        super.onSelectionRestored()
        selectionRestored()
    }

    override fun onItemStateChanged(key: T, selected: Boolean) {
        super.onItemStateChanged(key, selected)
        itemStateChanged(key, selected)
    }

    override fun onSelectionChanged() {
        super.onSelectionChanged()
        selectionChanged()
    }

    @SuppressLint("RestrictedApi")
    override fun onSelectionCleared() {
        super.onSelectionCleared()
        selectionCleared()
    }
}