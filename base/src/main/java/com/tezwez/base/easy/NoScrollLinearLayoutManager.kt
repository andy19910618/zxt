package com.be.base.easy

import android.content.Context

import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager


class NoScrollLinearLayoutManager : LinearLayoutManager {
    constructor(context: Context) : super(context)

    constructor(context: Context, orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

    override fun canScrollVertically(): Boolean {
        return false
    }
}