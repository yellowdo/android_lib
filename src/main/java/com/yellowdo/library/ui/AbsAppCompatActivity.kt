package com.yellowdo.library.ui

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.yellowdo.library.BR
import com.yellowdo.library.R
import com.yellowdo.library.ext.toastShow
import com.yellowdo.library.util.ProgressDialogUtil
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.reflect.ParameterizedType


abstract class AbsAppCompatActivity<B : ViewDataBinding, V : AbsViewModel>(layoutId: Int) : AppCompatActivity(layoutId) {
    protected val binding by lazy { DataBindingUtil.bind<B>((window.decorView.findViewById(android.R.id.content) as ViewGroup).getChildAt(0))!! }

    @Suppress("UNCHECKED_CAST")
    protected val vm by viewModel(
        clazz = ((javaClass.genericSuperclass as ParameterizedType?)
            ?.actualTypeArguments
            ?.get(1) as Class<V>).kotlin
    )

    protected open var bindingApply: (B.() -> Unit)? = null
    protected open var vmApply: (V.() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(binding) {
            lifecycleOwner = this@AbsAppCompatActivity
            setVariable(BR.vm, vm)
            bindingApply?.invoke(this)
        }

        with(vm) vm@{
            lifecycle.addObserver(this@vm)
            toast.observe(this@AbsAppCompatActivity) { toastShow(it) }
            finish.observe(this@AbsAppCompatActivity) { finish() }
            backPressed.observe(this@AbsAppCompatActivity) { onBackPressed() }
            startActivity.observe(this@AbsAppCompatActivity) {
                startActivity(Intent(this@AbsAppCompatActivity, it.first))
                if (it.second) finish()
            }
            startActivityIntent.observe(this@AbsAppCompatActivity) {
                startActivity(Intent(this@AbsAppCompatActivity, it.second).also(it.first))
                if (it.third) finish()
            }

            isLoading.observe(this@AbsAppCompatActivity, Observer {
                if (it) ProgressDialogUtil.show(this@AbsAppCompatActivity) { setMessage(getString(R.string.please_wait)) }
                else ProgressDialogUtil.dismiss()
            })
            vmApply?.invoke(this)
        }
    }

    private var backPressedTime: Long = 0
    fun delayBackPressed(block: () -> Unit = { finish() }) {
        val tempTime = System.currentTimeMillis()
        when (tempTime - backPressedTime) {
            in 0..2000 -> block()
            else -> {
                backPressedTime = tempTime
                toastShow(getString(R.string.backkey_message), Toast.LENGTH_SHORT)
            }
        }
    }
}
