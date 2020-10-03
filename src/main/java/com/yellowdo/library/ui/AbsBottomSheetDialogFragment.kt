package com.yellowdo.library.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.reflect.ParameterizedType

abstract class AbsBottomSheetDialogFragment<B : ViewDataBinding, V : AbsViewModel>(private val layoutId: Int) : BottomSheetDialogFragment() {
    protected lateinit var binding: B
        private set

    @Suppress("UNCHECKED_CAST")
    protected val viewModel by viewModel(
        clazz = ((javaClass.genericSuperclass as ParameterizedType?)
            ?.actualTypeArguments
            ?.get(1) as Class<V>).kotlin
    )

    protected open var bindingInject: (B.() -> Unit)? = null
    protected open var bindingApply: (B.() -> Unit)? = null
    protected open var vmApply: (V.() -> Unit)? = null
    protected open var onDismiss: (() -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.root
    }

    @Suppress("DEPRECATION")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            bindingApply?.invoke(this)
        }
        with(viewModel) {
            lifecycle.addObserver(this)
            finish.observe(this@AbsBottomSheetDialogFragment) { dismiss() }
            vmApply?.invoke(this)
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        manager.beginTransaction().let {
            it.add(this, tag)
            it.commitAllowingStateLoss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        onDismiss?.invoke()
        super.onDismiss(dialog)
    }
}
