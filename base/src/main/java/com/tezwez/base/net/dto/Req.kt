package com.tezwez.base.net.dto



var APPID = ""
var DEVICEID = ""
var OS = "android"
var OSVERSION = ""
var SCREENSIZE = ""
var APIVERSION = ""
var REQUESTID = ""

//公用字段拓展预留
open class Req(
//                    val appId: String = APPID,
//                   val deviceID: String = DEVICEID,
//                   val os: String = OS,
//                   val osVersion: String= OSVERSION,
//                   val screenSize: String= SCREENSIZE,
//                   val timestamp: String= System.currentTimeMillis().toString(),
//                   val apiVersion: String= APIVERSION,
//                   val requestId: String= REQUESTID,
//                   val token:String = Hawk.get<String>(KEY_TOKEN)?:""
               ): HashMap<String, Any>(){
    init {
//        putParams("AppID",appId)
//        putParams("DeviceID",deviceID)
//        putParams("Os",os)
//        putParams("OsVersion",osVersion)
//        putParams("ScreenSize",screenSize)
//        putParams("Timestamp",timestamp)
//        putParams("ApiVersion",apiVersion)
//        putParams("RequestId",requestId)
//        putParams("token",token)
    }


     fun putParams(key:String, value:Any?) {
        if (key!=null&&key?.isNotBlank()&&value!=null){
            put(key, value)
        }

    }




}