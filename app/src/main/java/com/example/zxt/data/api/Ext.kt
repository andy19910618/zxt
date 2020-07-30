package com.example.zxt.data.api

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager

/**
 * Description : Ext
 * Author      : jovelin
 * Version     : 2.0
 * Create Date : 2019-09-15 11:12
 */

@SuppressLint("MissingPermission")
fun Context.isConnected(): Boolean {
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = cm.activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnected
}