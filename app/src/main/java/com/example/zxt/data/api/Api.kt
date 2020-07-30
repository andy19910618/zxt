package com.tezwez.club.data.api

import com.tezwez.base.net.dto.Rep
import com.tezwez.base.net.dto.Req
import com.tezwez.club.data.dto.CountBean
import com.tezwez.club.data.dto.DataInfo
import com.tezwez.club.data.dto.GetCaveat
import com.tezwez.club.data.dto.MyData
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST


interface Api {
    /**
     * 获取列表
     */
    @POST("/mall/alarmMsg/getNewest.do")
    fun getNewest(@Body req: Req): Single<Rep<List<MyData>>>


    @POST("/mall/alarmMsg/getTodayList.do")
    fun getList(@Body req: Req): Single<Rep<DataInfo>>

    @POST("/mall/alarmMsg/getListByTime.do")
    fun getListByTime(@Body req: Req): Single<Rep<List<CountBean>>>

    @POST("/mall/alarmMsg/getListByHour.do")
    fun getListByHour(@Body req: Req): Single<Rep<List<CountBean>>>

    @POST("/mall/alarmMsg/getListByMonth.do")
    fun getListByMonth(@Body req: Req): Single<Rep<List<CountBean>>>

    @POST("/mall/alarmMsg/getListByYear.do")
    fun getListByYear(@Body req: Req): Single<Rep<List<CountBean>>>

    @POST("/mall/alarmMsg/getCaveat.do")
    fun getCaveat(@Body req: Req): Single<Rep<List<GetCaveat>>>

}