package com.yellowdo.library.ext

import androidx.lifecycle.*
import com.yellowdo.library.util.Event

inline fun <X, Y> LiveData<X>.map(crossinline transform: (X) -> Y): LiveData<Y> = Transformations.map(this) { transform(it) }
inline fun <X, Y> LiveData<X>.switchMap(crossinline transform: (X) -> LiveData<Y>): LiveData<Y> = Transformations.switchMap(this) { transform(it) }

@Suppress("NOTHING_TO_INLINE")
inline fun <X> LiveData<X>.distinctUntilChanged(): LiveData<X> = Transformations.distinctUntilChanged(this)

inline fun <T> LiveData<Event<T>>.observeEvent(owner: LifecycleOwner, crossinline onEventUnhandledContent: (T) -> Unit) {
    observe(owner, Observer { it?.getContentIfNotHandled()?.let(onEventUnhandledContent) })
}

/**
 * Sets the value to the result of a function that is called when both `LiveData`s have data
 * or when they receive updates after that.
 */
fun <T, A, B> LiveData<A>.combineAndCompute(other: LiveData<B>, onChange: (A, B) -> T): MediatorLiveData<T> {
    var source1emitted = false
    var source2emitted = false

    val result = MediatorLiveData<T>()

    val mergeF = {
        val source1Value = this.value
        val source2Value = other.value

        if (source1emitted && source2emitted) {
            result.value = onChange.invoke(source1Value!!, source2Value!!)
        }
    }

    result.addSource(this) { source1emitted = true; mergeF.invoke() }
    result.addSource(other) { source2emitted = true; mergeF.invoke() }

    return result
}