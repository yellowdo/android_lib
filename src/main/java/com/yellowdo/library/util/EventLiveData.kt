package com.yellowdo.library.util

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class EventLiveData<T>(private var onEventUnhandledContent: ((T) -> Unit)? = null) : MutableLiveData<Event<T>>(), Observer<Event<T>> {
    override fun onChanged(event: Event<T>?) {
        onEventUnhandledContent?.let { EventObserver(it).onChanged(event) }
    }

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in Event<T>>) {
        super.observe(owner, Observer { t -> observer.onChanged(t) })
    }

    @MainThread
    fun observe(owner: LifecycleOwner, obsv: ((T) -> Unit)? = null) {
        observe(owner, this.apply { obsv?.let { onEventUnhandledContent = it } })
    }

    @MainThread
    fun setEvent(t: T) = super.setValue(Event(t))

    fun postEvent(t: T) = postValue(Event(t))
}

sealed class Empty {
    object Call : Empty()
}

