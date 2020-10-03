package com.yellowdo.library.ui

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import com.yellowdo.library.ext.toastShow
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class AbsFragment<B : ViewDataBinding, V : AbsViewModel>(layoutResId: Int, viewModelCls: Class<V>) : Fragment(layoutResId) {
    protected val binding by lazy { DataBindingUtil.bind<B>(requireView())!! }
    protected val viewModel by viewModel(clazz = viewModelCls.kotlin)
    protected open var bindingApply: (B.() -> Unit)? = null
    protected open var vmApply: (V.() -> Unit)? = null
    protected open var onBackPressed: OnBackPressedCallback.() -> Unit = {}

    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            setVariable(BR.vm, viewModel)
            bindingApply?.invoke(this)
        }

        with(viewModel) {
            lifecycle.addObserver(this)
            startActivity.observe(viewLifecycleOwner) {
                startActivity(Intent(activity, it.first))
                if (it.second) activity?.finish()
            }
            toast.observe(viewLifecycleOwner) { toastShow(it) }
            finish.observe(viewLifecycleOwner) { requireActivity().finish() }

            isLoading.observe(viewLifecycleOwner, Observer {
                if (it) ProgressDialogUtil.show(requireActivity()) { setMessage("잠시만 기다려 주세요...") }
                else ProgressDialogUtil.dismiss()
            })

            startActivityIntent.observe(viewLifecycleOwner) {
                startActivity(Intent(requireActivity(), it.second).apply(it.first))
                if (it.third) requireActivity().finish()
            }

            hideKeyboard.observe(viewLifecycleOwner) { hideSoftInputFromWindow() }

            vmApply?.invoke(this)
        }
    }

    @Suppress("DEPRECATION")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(owner = viewLifecycleOwner, onBackPressed = onBackPressed)
    }

    private var backPressedTime: Long = 0
    fun delayDestroy(block: () -> Unit = {}) {
        val tempTime = System.currentTimeMillis()
        when (tempTime - backPressedTime) {
            in 0..2000 -> block()
            else -> {
                backPressedTime = tempTime
                toastShow(getString(R.string.exit_message), Toast.LENGTH_SHORT)
            }
        }
    }
}
