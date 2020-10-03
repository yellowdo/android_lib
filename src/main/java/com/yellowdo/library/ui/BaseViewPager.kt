package com.yellowdo.library.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout


class BaseViewPager : ViewPager {
    var swipeEnable: Boolean = false
    var tabLayout: TabLayout? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return if (swipeEnable) super.onInterceptTouchEvent(ev) else
            when (ev.actionMasked) {
                MotionEvent.ACTION_MOVE, MotionEvent.ACTION_DOWN -> false
                else -> {
                    if (super.onInterceptTouchEvent(ev)) super.onTouchEvent(ev)
                    false
                }
            }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return if (swipeEnable) super.onTouchEvent(ev) else ev.actionMasked != MotionEvent.ACTION_MOVE && super.onTouchEvent(ev)
    }

}
