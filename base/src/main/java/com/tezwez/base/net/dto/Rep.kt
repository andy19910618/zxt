package com.tezwez.base.net.dto



const val COMMON_SUC_CODE =0 //只有0是成功
data class Rep<T>(val errmsg: String,
                  val errcode: Int,
                  val data: T) {

    fun isSuccessful() = errcode == COMMON_SUC_CODE
}
















