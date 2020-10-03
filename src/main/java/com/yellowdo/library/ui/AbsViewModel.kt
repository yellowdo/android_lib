package com.yellowdo.library.ui

import android.content.Intent
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yellowdo.library.ext.launchCatching
import com.yellowdo.library.ext.plusAssign
import com.yellowdo.library.util.Empty
import com.yellowdo.library.util.EventLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope

abstract class AbsViewModel : ViewModel(), LifecycleObserver {
    var toast = EventLiveData<Any>()
    var finish = EventLiveData<Empty>()
    var backPressed = EventLiveData<Empty>()
    var startActivity = EventLiveData<Pair<Class<*>, Boolean>>()
    var startActivityIntent = EventLiveData<Triple<(Intent) -> Unit, Class<*>, Boolean>>()
    private val compositeDisposable = CompositeDisposable()
    var isLoading = MutableLiveData<Boolean>()
    protected open var exceptionHandler: (Throwable) -> Unit = {}

    fun addDisposable(disposable: Disposable) {
        compositeDisposable += disposable
    }

    fun CoroutineScope.launchCatchingWithLoading(action: suspend CoroutineScope.() -> Unit) {
        launchCatching(
            block = {
                isLoading.postValue(true)
                action()
                isLoading.postValue(false)
            }, exceptionHandler = exceptionHandler
        )
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}
