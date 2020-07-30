package com.be.base.easy

import android.content.Context

import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager


class NoScrollGridLayoutManager : GridLayoutManager {
    constructor(context: Context,spanCount:Int ) : super(context,spanCount)

    constructor(context: Context, spanCount: Int, orientation: Int, reverseLayout: Boolean) : super(context, spanCount,orientation, reverseLayout) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

    override fun canScrollVertically(): Boolean {
        return false
    }
}