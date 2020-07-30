package com.tezwez.club.data.dto

/**
 * Created by dell on 2019/1/18.
 */

data class DataInfo(
    val endRow: String,
    val firstPage: String,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean,
    val isFirstPage: Boolean,
    val isLastPage: Boolean,
    val lastPage: String,
    val list: List<MyData>,
    val navigatePages: String,
    val navigatepageNums: List<String>,
    val nextPage: String,
    val orderBy: Any,
    val pageNum: String,
    val pageSize: String,
    val pages: String,
    val prePage: String,
    val size: String,
    val startRow: String,
    val total: String
)

data class MyData(
    val alarmPictureName: String,
    val alarmReason: String,
    val alarmTime: Long,
    val alarmType: String,
    val cellid: String,
    val id: String,
    val parse: String,
    val pictureType: String,
    val sensorid: String,
    val warnName: String
)

data class CountBean(
    val day: String,
    val sum: Int
)


data class GetCaveat(
    val caveatType: String,
    val proportion: Any,
    val sum: Int,
    val warnName: String
)



