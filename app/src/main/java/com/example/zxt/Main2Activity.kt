package com.example.zxt

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import com.example.zxt.newpage.BarChartManager
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.android.synthetic.main.activity_image.*

class Main2Activity : PermissionActivity() , OnChartValueSelectedListener {
    override fun onNothingSelected() {}
    override fun onValueSelected(e: Entry?, h: Highlight?) {}
    var yVals = mutableListOf<BarEntry>()

    protected lateinit var tfLight: Typeface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        tfLight = Typeface.createFromAsset(assets, "OpenSans-Light.ttf")
        showBarChartAlong()
    }

    fun showBarChartAlong() {
        var barChartManager = BarChartManager(barChart1)
        yVals.add(BarEntry(1f, 80f))
        yVals.add(BarEntry(2f, 50f))
        yVals.add(BarEntry(3f, 60f))
        yVals.add(BarEntry(4f, 60f))
        yVals.add(BarEntry(5f, 70f))
        yVals.add(BarEntry(6f, 80f))
        yVals.add(BarEntry(7f, 10f))
        yVals.add(BarEntry(8f, 20f))
        yVals.add(BarEntry(9f, 30f))
        yVals.add(BarEntry(10f, 40f))
        yVals.add(BarEntry(11f, 50f))
        yVals.add(BarEntry(12f, 60f))
        var label = "";
        barChartManager.showBarChart(yVals, label, Color.parseColor("#233454"));
    }
}
