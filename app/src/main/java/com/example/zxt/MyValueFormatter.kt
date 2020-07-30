package com.example.zxt

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.ValueFormatter

class MyValueFormatter(private val suffix: String) : ValueFormatter() {

//    private val mFormat: DecimalFormat

    init {
//        mFormat = DecimalFormat("###,###,###,##0.0")
    }

    override fun getFormattedValue(value: Float): String {
        return value.toString()
    }

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return if (axis is XAxis) {
            value.toString()
        } else if (value > 0) {
            value.toString()
        } else {
            value.toString()
        }
    }
}
