package com.yellowdo.library.bindingadapter

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.animation.AnimationUtils
import androidx.databinding.BindingAdapter
import com.yellowdo.library.R

@BindingAdapter(value = ["selected"])
fun View.setSelected(selected: Boolean) {
    isSelected = selected
}

@BindingAdapter(value = ["enabled"])
fun View.setEnabled(enabled: Boolean) {
    isEnabled = enabled
}

@BindingAdapter(value = ["visible"])
fun View.setVisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter(value = ["onLongClick"])
fun View.OnLongClickListener(listener: (View) -> Boolean = { false }) {
    setOnLongClickListener { listener.invoke(it) }
}

@BindingAdapter(value = ["onClick"])
fun View.onClickListener(listener: (View) -> Any? = {}) {
    setOnClickListener { listener.invoke(it) }
}

@BindingAdapter(value = ["onClick", "position"])
fun View.onItemClickListener(listener: ((View, Int) -> Any?)? = null, position: Int) {
    setOnClickListener { listener?.invoke(it, position) }
}

@BindingAdapter(value = ["animation"])
fun View.loadAnimation(scrolling: Int) {
    when (scrolling) {
        -1 -> animation = AnimationUtils.loadAnimation(context, R.anim.anim_from_bottom_d300)
        1 -> animation = AnimationUtils.loadAnimation(context, R.anim.anim_from_top_d300)
    }
}

@BindingAdapter(value = ["bgCornerRadius"])
fun View.setBackgroundCornerRadius(cornerRadius: Float) {
    background = background.getGradientDrawable(cornerRadius * resources.displayMetrics.density)
}

fun Drawable.getGradientDrawable(cornerRadius: Float): GradientDrawable {
    return when (this) {
        is GradientDrawable -> {
            this
        }
        else -> {
            GradientDrawable().also {
                if (this is ColorDrawable) {
                    it.alpha = this.alpha
                    it.setColor(this.color)
                }
            }
        }
    }.apply { this.cornerRadius = cornerRadius }
}
