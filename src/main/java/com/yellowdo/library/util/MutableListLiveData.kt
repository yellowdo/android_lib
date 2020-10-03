package com.yellowdo.library.util

import androidx.lifecycle.MutableLiveData

class MutableListLiveData<T> : MutableLiveData<MutableList<T>>() {
    init {
        value = mutableListOf()
    }

    fun add(item: T) {
        val items = value
        items!!.add(item)
        postValue(items)
    }

    fun addAll(list: MutableList<T>) {
        val items = value
        items!!.addAll(list)
        postValue(items)
    }

    fun clearAndAddAll(list: MutableList<T>) {
        val items = mutableListOf<T>()
        items.addAll(list)
        postValue(items)
    }

    fun clear(notify: Boolean) {
        val items = value
        items!!.clear()
        if (notify) {
            postValue(items)
        }
    }

    fun containPosition(predicate: (T) -> Boolean): Int {
        value?.forEachIndexed { index, t ->
            if (predicate.invoke(t)) return index
        }
        return -1
    }

    fun update(position: Int, item: T) {
        val items = value
        items!![position] = item
        postValue(items)
    }

    fun remove(item: T) {
        val items = value
        items!!.remove(item)
        postValue(items)
    }

    fun notifyChange() {
        val items = value
        postValue(items)
    }

    override fun toString(): String {
        return value.toString()
    }
}
