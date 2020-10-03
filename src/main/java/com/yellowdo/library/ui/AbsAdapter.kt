package com.yellowdo.library.ui

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class AbsAdapter<T, B : ViewDataBinding>(private val layoutId: Int) : RecyclerView.Adapter<OpenViewHolder<B>>() {
    protected val items = mutableListOf<T>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = OpenViewHolder<B>(parent, layoutId)

    override fun getItemId(position: Int): Long = position.toLong()
    override fun getItemCount(): Int = items.size

    fun addAll(listAdd: MutableList<T>) {
        val oldSize = items.size
        items.addAll(listAdd)
        notifyItemRangeChanged(oldSize, items.size)
    }

    fun clearAndAddAll(listAdd: MutableList<T>?) {
        items.clear()
        listAdd?.takeIf { listAdd.isNotEmpty() }?.apply { items.addAll(this) }
    }

    fun updateList(newList: MutableList<T>) {
        val result = DiffUtil.calculateDiff(diffUtilCallback(this.items, newList))
        clearAndAddAll(newList)
        result.dispatchUpdatesTo(this)
    }

    fun replaceAll(items: MutableList<T>) {
        this.items.run {
            clear()
            addAll(items)
        }
        notifyDataSetChanged()
    }


    fun add(item: T) {
        items.add(item)
        notifyDataSetChanged()
    }

    fun update(idx: Int, item: T) {
        items[idx] = item
    }

    fun get(position: Int): T = items[position]

    fun clear() {
        items.run {
            clear()
            notifyDataSetChanged()
        }
    }

    abstract var areItems: (T, T) -> Boolean
    abstract var areContents: (T, T) -> Boolean

    private fun diffUtilCallback(oldList: MutableList<T>, newList: MutableList<T>): DiffUtil.Callback {
        return object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return areItems.invoke(oldList[oldItemPosition], newList[newItemPosition])
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return areContents.invoke(oldList[oldItemPosition], newList[newItemPosition])
            }

            override fun getOldListSize(): Int = oldList.size

            override fun getNewListSize(): Int = newList.size
        }
    }

}
