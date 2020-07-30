package com.example.zxt

import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.formatter.ValueFormatter

/**
 * Created by philipp on 02/06/16.
 */
class DayAxisValueFormatter(private val chart: BarLineChartBase<*>) : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {

        return value.toString()
    }
}
