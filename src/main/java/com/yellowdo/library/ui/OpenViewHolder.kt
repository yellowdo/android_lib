package com.yellowdo.library.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class OpenViewHolder<B : ViewDataBinding>(parent: ViewGroup?, @LayoutRes layoutRes: Int) : RecyclerView.ViewHolder(LayoutInflater.from(parent?.context).inflate(layoutRes, parent, false)) {
    val binding: B = DataBindingUtil.bind(itemView)!!
}
