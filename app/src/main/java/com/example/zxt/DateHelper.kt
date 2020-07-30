package com.example.zxt

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


public fun getDay(daySum:Int) :ArrayList<String>{
    val arrayList = ArrayList<String>()
    for (temp in 1..daySum){
        val  calendar =Calendar. getInstance()
        calendar.add(Calendar.DATE, -temp) //向前走一天
        val date= calendar.time
        arrayList.add(DateUtils.dateToString(date,DateUtils.type1))

    }
    arrayList.reverse()

    arrayList.forEach{
        println(it)
    }
    System.out.println(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
    return arrayList
}

fun main(args: Array<String>) {
    getDay(7)
}