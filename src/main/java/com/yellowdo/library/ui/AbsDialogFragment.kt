package com.yellowdo.library.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

abstract class AbsDialogFragment<B : ViewDataBinding>(private val layoutId: Int) : DialogFragment() {
    protected lateinit var binding: B
        private set

    protected open var bindingApply: (B.() -> Unit)? = null
    protected open var onCreateView: (() -> Unit?)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        with(binding) {
            lifecycleOwner = this@AbsDialogFragment
            bindingApply?.invoke(binding)
        }
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        onCreateView?.invoke()
        return binding.root
    }


    override fun show(manager: FragmentManager, tag: String?) {
        manager.beginTransaction().let {
            it.add(this, tag)
            it.commitAllowingStateLoss()
        }
    }
}
