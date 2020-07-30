package com.example.zxt

import android.content.BroadcastReceiver
import android.content.Intent
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.be.base.easy.EasyAdapter
import com.be.base.view.ErrorDialog
import com.example.zxt.DateUtils.datePattern
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.model.GradientColor
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.tezwez.base.helper.click
import com.tezwez.base.helper.loge
import com.tezwez.club.data.dto.CountBean
import com.tezwez.club.data.dto.GetCaveat
import com.tezwez.club.data.dto.MyData
import com.tezwez.club.data.vm.ApiViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_msg.view.*
import org.jetbrains.anko.toast
import org.koin.android.viewmodel.ext.android.viewModel
import java.math.BigDecimal
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class MainActivity : PermissionActivity(), OnChartValueSelectedListener {
    lateinit var easyAdapter: EasyAdapter<MyData>
    var mList = mutableListOf<MyData>()
    var mAllList = mutableListOf<MyData>()
    val mApiViewModel: ApiViewModel by viewModel()
    var pageNum = 1

    var hasNextPage: Boolean? = true
    var isAuto: Boolean? = false //自动轮寻 查数据
    var hasPreviousPage: Boolean? = false
    var total = 1
    private var temp: Long = 0
    private var receiver: BroadcastReceiver? = null
    private var newest: BigDecimal = BigDecimal(0)
    private var clicks = 0

    private var mCursorTimerTask: TimerTask? = null
    private var mCursorTimer: Timer? = null
    private var mCursorDuration: Long = 10000
    protected lateinit var tfLight: Typeface
    var position1 = -1
    var position2 = -1
    var position4 = -1
    var mList1 = mutableListOf<String>()
    var mList2 = mutableListOf<String>()
    var mList4 = mutableListOf<String>()
    protected lateinit var tfRegular: Typeface
    protected val errorType =
        arrayOf("一级告警", "二级告警", "三级告警")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tfLight = Typeface.createFromAsset(assets, "OpenSans-Light.ttf")
        tfRegular = Typeface.createFromAsset(assets, "OpenSans-Regular.ttf")
        initRv()
        initEvent()
        initEvent2()
        initReceiver()
        getDataInfo()
        getToMouth()
        initChart1()
        initChart2()
        getData(1, 0)
        initPieChart()
    }

    fun initRv() {
        recycleView.layoutManager = LinearLayoutManager(this)
        easyAdapter = EasyAdapter(R.layout.item_msg, { itmeView, position, item ->
            itmeView.tvContent.text = if(item?.warnName.isNullOrEmpty()) "其他" else item.warnName
            itmeView.tvTime.text = DateUtils.convertTimeToString(item.alarmTime, datePattern)
            when (item.pictureType) {
                "1" -> {
                    itmeView.tvType.text = "一级警告"
                }
                "2" -> {
                    itmeView.tvType.text = "二级警告"
                }
                "3" -> {
                    itmeView.tvType.text = "三级警告"
                }
            }
            itmeView.tvShowImg.click {
                showDialog(item.alarmPictureName, item.parse)
            }
        }, emptyList())
        recycleView.adapter = easyAdapter
    }


    fun initChart1() {
        initTu(chart, 4)
        mApiViewModel.getListByHour(24).observe(this, androidx.lifecycle.Observer {
            if (it?.isEmpty()?.not() == true) {
                var num = 0
                for (data in it) {
                    num = num + data.sum
                }
                tvNum.text = "$num"
                setData(chart, it, 4)
            }
        })
    }

    fun initChart2() {
//        initTu(chart2, 1)
        day.isSelected = true
        month.isSelected = false
        mApiViewModel.getListByTimeHistory(7).observe(this, androidx.lifecycle.Observer {
            if (it?.isEmpty()?.not() == true) {
                initTu(chart2, 1)
                setData(chart2, it, 1)
            }
        })
    }


    fun getNewest() {
        mApiViewModel.getNewest().observe(this, androidx.lifecycle.Observer {
            if (it?.isNotEmpty() == true) {
                if (BigDecimal(it?.get(0)?.id) > newest) {
                    if (isAuto == true) {
                        showDialog(it?.get(0)?.alarmPictureName, it?.get(0)?.parse)
                    }
                    newest = BigDecimal(it?.get(0)?.id)
                    getDataInfo()
                    getToMouth()
                    initChart1()
                    initChart2()
                    getData(1, 0)
                }
            }
        })
    }

    fun getDataInfo() {
        if (pageNum > 4) return
        mApiViewModel.getList(1, 30).observe(this, androidx.lifecycle.Observer {

            if (it != null) {
                if (!it.list.isEmpty()) {
                    mAllList.clear()
                    mAllList.addAll(it?.list)
                    mAllList.sortBy { it.pictureType }
                }
            }

            mList.clear()
            for (i in 8 * (pageNum - 1)..if (8 * pageNum < (mAllList.size - 1)) (8 * pageNum - 1) else (mAllList.size - 1)) {
                if (i < mAllList.size)
                    mList.add(mAllList[i])
            }

            easyAdapter.submitList(mList)
            if (mList.isNullOrEmpty()) {
                noDateTv.visibility = View.VISIBLE
            } else {
                noDateTv.visibility = View.GONE
            }


        })
    }


    fun getToMouth() {

        mApiViewModel.getToMouth(BigDecimal(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).toInt())
            .observe(this, androidx.lifecycle.Observer {
                if (it?.isNotEmpty() == true) {
                    var num = 0
                    for (data in it) {
                        num = num + data.sum
                    }
                    tvNumAll.text = "$num"
                }
            })
    }


    private fun setData(lineChart: BarChart, list: List<CountBean>, type: Int) {
        val values = ArrayList<BarEntry>()

        when (type) {
            1 -> {
                val day7list = getDay(7)
                day7list?.forEachIndexed { index, str ->
                    var sum = 0f
                    list?.forEach { countBean ->
                        if (countBean?.day == str) {
                            sum = countBean?.sum?.toFloat()
                        } else {

                        }
                    }

                    values.add(
                        BarEntry(
                            index.toFloat(),
                            sum,
                            resources.getDrawable(R.drawable.star)
                        )
                    )

                }

            }
            2 -> {

                val day7list = getDay(30)
                day7list?.forEachIndexed { index, str ->
                    var sum = 0f
                    list?.forEach { countBean ->
                        if (countBean?.day == str) {
                            sum = countBean?.sum?.toFloat()
                        } else {

                        }
                    }
                    values.add(
                        BarEntry(
                            index.toFloat(),
                            sum,
                            resources.getDrawable(R.drawable.star)
                        )
                    )
                }

            }
            3 -> {

            }
            4 -> {//24小时  2019101200
                for (temp in 0..23) {
                    var sum = 0f
                    list?.forEach { countBean ->
                        var value =
                            countBean.day.substring(countBean.day.length - 2, countBean.day.length)
                        if (BigDecimal(value) == BigDecimal(temp)) {
                            sum = countBean?.sum?.toFloat()
                        } else {

                        }
                    }
                    values.add(
                        BarEntry(
                            temp.toFloat(),
                            sum,
                            resources.getDrawable(R.drawable.star)
                        )
                    )
                }

            }

        }




        for (value in values) {
            Log.d("tag>>>>>>44", "$type --- ${value.x}")
        }

        val set1: BarDataSet

        if (lineChart.data != null && lineChart.data.dataSetCount > 0) {
            set1 = lineChart.data.getDataSetByIndex(0) as BarDataSet
            set1.values = values
            set1.setDrawValues(false)
            lineChart.data.notifyDataChanged()
            lineChart.notifyDataSetChanged()

        } else {
            set1 = BarDataSet(values, "告警次数")
            set1.setDrawIcons(false)
            set1.setDrawValues(false)
            val startColor1 = ContextCompat.getColor(this, android.R.color.holo_orange_light)
            val startColor2 = ContextCompat.getColor(this, android.R.color.holo_blue_light)
            val startColor3 = ContextCompat.getColor(this, android.R.color.holo_orange_light)
            val startColor4 = ContextCompat.getColor(this, android.R.color.holo_green_light)
            val startColor5 = ContextCompat.getColor(this, android.R.color.holo_red_light)
            val endColor1 = ContextCompat.getColor(this, android.R.color.holo_blue_dark)
            val endColor2 = ContextCompat.getColor(this, android.R.color.holo_purple)
            val endColor3 = ContextCompat.getColor(this, android.R.color.holo_green_dark)
            val endColor4 = ContextCompat.getColor(this, android.R.color.holo_red_dark)
            val endColor5 = ContextCompat.getColor(this, android.R.color.holo_orange_dark)

            val gradientColors = ArrayList<GradientColor>()
            gradientColors.add(GradientColor(startColor1, endColor1))
            gradientColors.add(GradientColor(startColor2, endColor2))
            gradientColors.add(GradientColor(startColor3, endColor3))
            gradientColors.add(GradientColor(startColor4, endColor4))
            gradientColors.add(GradientColor(startColor5, endColor5))

            set1.gradientColors = gradientColors

            val dataSets = ArrayList<IBarDataSet>()
            dataSets.add(set1)

            val data = BarData(dataSets)
            data.setValueTextSize(10f)
            data.setValueTypeface(tfLight)
            data.barWidth = 0.9f

            lineChart.data = data
            lineChart.setVisibleXRangeMaximum(30f)
            lineChart.invalidate()
        }
    }

    public fun getDay(daySum: Int): ArrayList<String> {
        val arrayList = ArrayList<String>()
        for (temp in 0..daySum - 1) {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DATE, -temp) //向前走一天
            val date = calendar.time
            arrayList.add(DateUtils.dateToString(date, DateUtils.type1))

        }
        arrayList.reverse()
        if (daySum == 7) {
            mList1 = arrayList
        } else if (daySum == 30) {
            mList2 = arrayList
        }
        return arrayList
    }

    override fun onNothingSelected() {}
    override fun onValueSelected(e: Entry?, h: Highlight?) {}

    private fun initEvent() {
        mExit?.click {
            System.exit(0)
        }
        btnFrist.click {
            if (pageNum > 1) {
                isAuto = false
                temp = 0
                pageNum = 1
                getDataInfo()
            } else {
                toast("已经是第一页了")
            }
        }
        btnPre.click {
            if (pageNum > 1) {
                isAuto = false
                temp = 0
                pageNum--
                getDataInfo()
            } else {
                toast("已经是第一页了")
            }
        }
        btnNext.click {
            if (pageNum <= 4) {
                isAuto = false
                temp = 0
                pageNum++
                getDataInfo()
            } else {
                toast("已经是最后一页了")
            }
        }
        btnLast.click {
            if (pageNum == 4) {
                toast("已经是最后一页了")
            } else {
                isAuto = false
                temp = 0
                pageNum = 4
                getDataInfo()
            }
//            startActivity(Intent(this, Main2Activity::class.java))
        }
        day.click {
            day.isSelected = true
            month.isSelected = false
//            year.isSelected = false
            mApiViewModel.getListByTimeHistory(7).observe(this, androidx.lifecycle.Observer {
                if (it?.isEmpty()?.not() == true) {

                    initTu(chart2, 1)
                    setData(chart2, it, 1)
                }
            })
        }
        month.click {
            day.isSelected = false
            month.isSelected = true
            mApiViewModel.getListByDay30(30).observe(this, androidx.lifecycle.Observer {
                if (it?.isEmpty()?.not() == true) {
                    initTu(chart2, 2)
                    setData(chart2, it, 2)
                }
            })
        }
//        year.click {
//            day.isSelected = false
//            month.isSelected = false
//            year.isSelected = true
//            mApiViewModel.getListByYear(10).observe(this, androidx.lifecycle.Observer {
//                if (it?.isEmpty()?.not() == true) {
//                    var num = 0
//                    for (data in it) {
//                        num = num + data.sum
//                    }
//                    tvNumAll.text = "$num"
//                    initTu(chart2, 3)
//                    setData(chart2, it, 3)
//                }
//            })
//        }
        tvTitleClick.click {
            clicks++
            if (clicks > 7) {
                clicks = 0
                startActivity(Intent(this, SecondActivity::class.java))
            }
        }
    }

    fun initReceiver() {

        mCursorTimerTask = object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    // 通过光标间歇性显示实现闪烁效果
                    clicks = 0
                    isAuto = true
                    pageNum = 1
                    temp = System.currentTimeMillis()
                    getNewest()
                }
            }
        }
        mCursorTimer = Timer()
        mCursorTimer?.scheduleAtFixedRate(mCursorTimerTask, 10000L, mCursorDuration)
    }


    fun showDialog(string: String, string2: String) {

        var dialog = ErrorDialog.Builder(this)
            .message(string, string2)
            .setNegativeButton { dialog ->
                dialog.dismiss()
            }.build()
        dialog.show()

        Observable.timer(5000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                dialog.dismiss()
            }
    }

    fun getMyData(resType: String): String {
        return when (resType) {
            "1" -> "靠墙"
            "2" -> "摸高"
            "4" -> "不学习"
            "8" -> "不睡觉"
            "16" -> "不放风"
            "32" -> "多人"
            "64" -> "提示信息"
            "128" -> "厕所区域停留超时"
            "256" -> "厕所区域多人"
            "512" -> "无人"
            else -> getOther(resType)
        }
    }


    fun getOther(resType: String): String {
        var resu: String = ""
        var string = Integer.toBinaryString(Integer.parseInt(resType))
        for ((index, value) in string.toCharArray().withIndex()) {
            if (value == '1') {
                loge("$resType --->>$index")
                resu = when (string.toCharArray().size - index) {
                    1 -> "靠墙"
                    2 -> "摸高"
                    3 -> "不学习"
                    4 -> "不睡觉"
                    5 -> "不放风"
                    6 -> "多人"
                    7 -> "提示信息"
                    8 -> "厕所区域停留超时"
                    9 -> "厕所区域多人"
                    10 -> "无人"
                    else -> "其他"
                } + (if (resu.isNotEmpty()) ("+ $resu") else resu)
            }
        }
        return resu
    }

    override fun onResume() {
        super.onResume()
        //todo  初始化数据
//        initChart1()
    }

    override fun onDestroy() {
        super.onDestroy()
        mCursorTimer?.cancel()
    }

    private fun initTu(lineChart: BarChart, type: Int) {
        position1 = -1
        lineChart.setScaleEnabled(true)
        lineChart.setScaleMinima(1.0f, 1.0f)
        lineChart.viewPortHandler.refresh(Matrix(), lineChart, true)
        // background color
        lineChart.setBackgroundColor(Color.WHITE)
        // disable description text
        lineChart.getDescription().setEnabled(false)
        // enable touch gestures
        lineChart.setTouchEnabled(true)
        // set listeners
        lineChart.setOnChartValueSelectedListener(this)
        lineChart.setDrawGridBackground(false)
        // create marker to display box when values are selected
        val mv = MyMarkerView(this, R.layout.custom_marker_view)
        mv.setType(type)
        // Set the marker to the lineChart
        mv.setChartView(lineChart)
        lineChart.marker = mv
        lineChart.setAutoScaleMinMaxEnabled(false)
        // enable scaling and dragging
        lineChart.isDragEnabled = true
        lineChart.setScaleEnabled(true)
        // lineChart.setScaleXEnabled(true);
        // lineChart.setScaleYEnabled(true);


        // force pinch zoom along both axis
        lineChart.setPinchZoom(true)

        val xAxis: XAxis
        run {
            // // MyData-Axis Style // //
            xAxis = lineChart.xAxis
            // vertical grid lines
            xAxis.enableGridDashedLine(10f, 10f, 0f)
            xAxis.setDrawGridLines(false)
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
            xAxis.setTextSize(5f)
        }

        xAxis.setValueFormatter(object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                Log.d(
                    "tag>>>>>>",
                    "$type ---$value----${if (mList1.size > value.toInt()) mList1[value.toInt()]?.substring(
                        mList1[value.toInt()].length - 2,
                        mList1[value.toInt()].length
                    ) else ""}"
                )
                return when (type) {
                    1 -> {
                        if (mList1.size > value.toInt()) mList1[value.toInt()]?.substring(
                            mList1[value.toInt()].length - 2,
                            mList1[value.toInt()].length
                        ) else ""
                    }
                    2 -> {
                        if (mList2.size > value.toInt()) mList2[value.toInt()]?.substring(
                            mList2[value.toInt()].length - 2,
                            mList2[value.toInt()].length
                        ) else ""
                    }
                    3 -> "${if (value < 10) 0 + (value.toInt()) else value.toInt()}年"
                    4 -> "${value.toInt()}"
                    else -> "${value.toInt()}"
                }
                Log.d(
                    "tag>>>>>>",
                    "$type --- ${if (mList1.size > position1) mList1[position1]?.substring(
                        mList1[position1].length - 2,
                        mList1[position1].length
                    ) else ""
                    }}"
                )
            }
        })

        val yAxis: YAxis
        run {
            // // Y-Axis Style // //
            yAxis = lineChart.axisLeft

            // disable dual axis (only use LEFT axis)
            lineChart.axisRight.isEnabled = false

            // horizontal grid lines
            yAxis.enableGridDashedLine(10f, 10f, 0f)
            yAxis.setDrawGridLines(false)
            // axis range
            yAxis.axisMaximum = when (type) {
                1 -> 600f
                2 -> 600f
                3 -> 60000f
                4 -> 60f
                else -> 600f
            }
            yAxis.axisMinimum = 0f
        }

        run {
            // // Create Limit Lines // //
            val llXAxis = LimitLine(9f, "Index 10")
            llXAxis.lineWidth = 4f
            llXAxis.enableDashedLine(10f, 10f, 0f)
            llXAxis.labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM
            llXAxis.textSize = 10f
//            llXAxis.typeface = tfRegular

            val ll1 = LimitLine(350f, "Upper Limit")
            ll1.lineWidth = 4f
            ll1.enableDashedLine(10f, 10f, 0f)
            ll1.labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
            ll1.textSize = 10f
//            ll1.typeface = tfRegular

            // draw limit lines behind data instead of on top
            yAxis.setDrawLimitLinesBehindData(true)
            xAxis.setDrawLimitLinesBehindData(true)
        }
//        xAxis.axisMinimum = when (type) {
//            1 -> 7f
//            2 -> 30f
//            3 -> 11f
//            4 -> 24f
//            else -> 7f
//        }
//
//        xAxis.mAxisMaximum = when (type) {
//            1 -> 7f
//            2 -> 12f
//            3 -> 21f
//            4 -> 24f
//            else -> 7f
//        }
        xAxis.labelCount = when (type) {
            1 -> 7
            2 -> 30
            4 -> 24
            else -> 7
        }

        // draw points over time
        lineChart.animateX(1500)

        // get the legend (only possible after setting data)
        val l = lineChart.legend

        // draw legend entries as lines
        l.form = Legend.LegendForm.LINE
    }


    fun initEvent2() {
        today.click {
            getData(1, 0)
        }

        day7.click {
            getData(7, 0)
        }

        day30.click {
            getData(30, 0)
        }
    }

    fun initPieChart() {
        pieChart.setUsePercentValues(true)
        pieChart.getDescription().setEnabled(true)
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

        pieChart.setDragDecelerationFrictionCoef(0.95f)
        ////设置隐藏饼图上文字，只显示百分比
        pieChart.setDrawSliceText(false)

        pieChart.setCenterTextTypeface(tfLight)
//        pieChart.setCenterText(generateCenterSpannableText())

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
        pieChart.setExtraLeftOffset(30f)
        //左上角的类型描述
        val l = pieChart.getLegend()
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM)
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT)
        l.setOrientation(Legend.LegendOrientation.VERTICAL)
        l.setDrawInside(false)
        l.formSize = 8f
        l.textSize = 8f
        l.setXEntrySpace(30f)
        l.setYEntrySpace(2f)
//        l.setYOffset(0f)

        // entry label styling
        pieChart.setEntryLabelColor(Color.RED)
        pieChart.setEntryLabelTypeface(tfRegular)
        pieChart.setEntryLabelTextSize(12f)
    }

    private fun generateCenterSpannableText(): SpannableString {
        val s = SpannableString("")
        s.setSpan(AbsoluteSizeSpan(14), 0, s.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return s
    }

    private fun setData(list: List<GetCaveat>) {
        val entries = ArrayList<PieEntry>()
        var total = list.sumBy { it.sum }

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart. n  you shi  einid di  pao
        for (i in 0 until list.size) {
            var data = list[i]
            entries.add(
                PieEntry(
                    (data.sum * 100).toFloat() / total,
                    data.warnName?:"",
                    resources.getDrawable(R.drawable.star)
                )
            )
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
        data.setValueTextSize(10f)
        data.setValueTextColor(Color.RED)
        data.setValueTypeface(tfLight)
        pieChart.setData(data)

        // undo all highlights
        pieChart.highlightValues(null)

        pieChart.invalidate()
    }
    fun getData(timeNumber:Int,type:Int){
        if(timeNumber == 1){
            today.isSelected = true
            day7.isSelected = false
            day30.isSelected = false
        } else if (timeNumber == 7) {
            today.isSelected = false
            day7.isSelected = true
            day30.isSelected = false
        } else if (timeNumber == 30) {
            today.isSelected = false
            day7.isSelected = false
            day30.isSelected = true
        }
        mApiViewModel.getCaveat(timeNumber, type).observe(this, androidx.lifecycle.Observer {
            if (it != null && it?.isNotEmpty()) {
                setData(it)
            }
        })
    }

    fun getErrorType(type: String): String {
        return when (type) {
            "1" -> "靠墙"
            "2" -> "摸高"
            "4" -> "不学习"
            "8" -> "不睡觉"
            "16" -> "不放风"
            "32" -> "多人"
            "64" -> "提示信息"
            "128" -> "厕所区域停留超时"
            "256" -> "厕所区域多人"
            "512" -> "无人"
            else -> "其它"
        }
    }


}
