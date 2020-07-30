package com.example.zxt

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.tezwez.base.helper.click
import com.tezwez.club.data.dto.GetCaveat
import com.tezwez.club.data.vm.ApiViewModel
import kotlinx.android.synthetic.main.activity_pie.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class PieActivity : AppCompatActivity() {
    protected lateinit var tfLight: Typeface
    protected lateinit var tfRegular: Typeface
    protected val errorType =
        arrayOf("一级告警", "二级告警", "三级告警")
    val mApiViewModel: ApiViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tfLight = Typeface.createFromAsset(assets, "OpenSans-Light.ttf")
        tfRegular = Typeface.createFromAsset(assets, "OpenSans-Regular.ttf")
        setContentView(R.layout.activity_pie)
        initEvent()
        initPieChart()
        getData(1,0)

    }

    fun initEvent(){
        day.click {
            getData(1,0)
        }
        month.click {
            getData(7,0)
        }
        year.click {
            getData(30,0)
        }
    }


    fun initPieChart(){
        pieChart.setUsePercentValues(true)
        pieChart.getDescription().setEnabled(true)
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

        pieChart.setDragDecelerationFrictionCoef(0.95f)
        ////设置隐藏饼图上文字，只显示百分比
        pieChart.setDrawSliceText(false)

        pieChart.setCenterTextTypeface(tfLight)
        pieChart.setCenterText(generateCenterSpannableText())

        pieChart.setDrawHoleEnabled(false)
        pieChart.setHoleColor(Color.WHITE)

        pieChart.setTransparentCircleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)

        pieChart.setHoleRadius(58f)
        pieChart.setTransparentCircleRadius(61f)

        pieChart.setDrawCenterText(true)

        pieChart.setRotationAngle(0f)
        // enable rotation of the pieChart by touch
        pieChart.setRotationEnabled(true)
        pieChart.setHighlightPerTapEnabled(true)

        // pieChart.setUnit(" €");
        // pieChart.setDrawUnitsInChart(true);

        // add a selection listener
//        pieChart.setOnChartValueSelectedListener(this)

        pieChart.animateY(1400, Easing.EaseInOutQuad)
        // pieChart.spin(2000, 0, 360);

        //左上角的类型描述
        val l = pieChart.getLegend()
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM)
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT)
        l.setOrientation(Legend.LegendOrientation.VERTICAL)
        l.setDrawInside(false)
        l.formSize = 20f
        l.textSize = 20f
        l.setXEntrySpace(20f)
        l.setYEntrySpace(20f)
//        l.setYOffset(0f)

        // entry label styling
        pieChart.setEntryLabelColor(Color.RED)
        pieChart.setEntryLabelTypeface(tfRegular)
        pieChart.setEntryLabelTextSize(20f)
    }

    private fun generateCenterSpannableText(): SpannableString {
        val s = SpannableString("告警类型分布")
        s.setSpan(AbsoluteSizeSpan(12),0,s.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return s
    }
    private fun setData(list : List<GetCaveat>) {
        val entries = ArrayList<PieEntry>()

        var total = list.sumBy { it.sum }

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (i in 0 until list.size) {
            var data = list[i]
//            entries.add(
//                PieEntry(
//                    (data.sum * 100).toFloat() / total,
//                    errorType[data.caveatType -1],
//                    resources.getDrawable(R.drawable.star)
//                )
//            )
        }

        val dataSet = PieDataSet(entries, "")

        dataSet.setDrawIcons(false)

        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        // add a lot of colors

        val colors = ArrayList<Int>()

        for (c in ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c)

        for (c in ColorTemplate.JOYFUL_COLORS)
            colors.add(c)

        for (c in ColorTemplate.COLORFUL_COLORS)
            colors.add(c)

        for (c in ColorTemplate.LIBERTY_COLORS)
            colors.add(c)

        for (c in ColorTemplate.PASTEL_COLORS)
            colors.add(c)

        colors.add(ColorTemplate.getHoloBlue())

        dataSet.colors = colors
        //dataSet.setSelectionShift(0f);

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter(pieChart))
        data.setValueTextSize(20f)
        data.setValueTextColor(Color.RED)
        data.setValueTypeface(tfLight)
        pieChart.setData(data)

        // undo all highlights
        pieChart.highlightValues(null)

        pieChart.invalidate()
    }
    fun getData(timeNumber:Int,type:Int){
        mApiViewModel.getCaveat(timeNumber,type).observe(this,androidx.lifecycle.Observer {
            setData(it)
        })
    }
}
