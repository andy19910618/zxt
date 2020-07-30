package com.tezwez.base.net

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.orhanobut.hawk.Hawk
import com.tezwez.base.BuildConfig
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit


fun retrofit(callFactory: Call.Factory, API_BASE_URL:String) = Retrofit.Builder()
    .callFactory(callFactory)
    .baseUrl(API_BASE_URL)
    .addConverterFactory(GsonConverterFactory.create(dateGson))
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .build()

fun okhttp() = OkHttpClient.Builder()
    .addInterceptor(interceptor)
    .addInterceptor(initLogInterceptor())
    .retryOnConnectionFailure(true)//失败重试一次 比较关键
    .connectTimeout(10, TimeUnit.SECONDS)
    .readTimeout(10,TimeUnit.SECONDS)
    .writeTimeout(10,TimeUnit.SECONDS)
    .build()

private const val CACHE_MAX_AGE = 60 * 60   // one hour
private const val DATE_PATTERN1 = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS"
private const val DATE_PATTERN2 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

private val dateGson: Gson
    get() = GsonBuilder()
        .registerTypeAdapter(
            Date::class.java,
            DateDeserializer(DATE_PATTERN1, DATE_PATTERN2)
        )
        .serializeNulls()
        .create()

private val interceptor: Interceptor
    get() = Interceptor { chain ->

        val request = chain.request()
        val builder = request.newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("charset","UTF-8")

//        val token = Hawk.get<String>(KEY_TOKEN)
//        if (token != null){
//            builder.addHeader("Authorization", token)
//        }
////            builder.addHeader("lang", getLang())
//        builder.addHeader("deviceId", DEVICE_ID)

        val response = chain.proceed(builder.build())
        val bytes = response?.body()?.bytes() ?: ""?.toByteArray()
        val build = response.newBuilder()
            .body(ResponseBody.create(MediaType.parse("UTF-8"), bytes)).build()
//        var responseCode = ""
//        var responseMsg = ""
//        try {
//            if (bytes!=null){
//                val returnData = JsonParser()?.parse(String(bytes))?.asJsonObject
//                responseCode = returnData?.get("errcode")?.asString?:"10000"
//                responseMsg = returnData?.get("errmsg")?.asString?:"server error"
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }finally {
//            //交给上层显示错误
//            if(responseCode?.isNotBlank()&&responseCode?.toInt() !=COMMON_SUC_CODE){
//                ErrorNotice.INSTANCE.notifyError(responseCode?.toInt(),responseMsg)
//            }else {
//
//            }
//        }
        build
    }
private fun initLogInterceptor(): HttpLoggingInterceptor {
    val interceptor = HttpLoggingInterceptor()
    if (BuildConfig.DEBUG){
        interceptor.level = HttpLoggingInterceptor.Level.HEADERS
        interceptor.level = HttpLoggingInterceptor.Level.BODY
    } else {
        interceptor.level =HttpLoggingInterceptor.Level.NONE
    }
    return interceptor
}

//class RetrofitFactory private constructor(){
//
//
//    /**
//     * 单例模式
//     */
//    companion object {
//        val instance : RetrofitFactory by lazy { RetrofitFactory() }
//    }
//
//    private val retrofit : Retrofit
//    private val interceptor: Interceptor
//
//    //初始化
//    init {
//        //通用拦截
//        interceptor = Interceptor { chain ->
//
//            val request = chain.request()
//            val builder = request.newBuilder()
//                    .addHeader("Content_Type", "application/json")
//                    .addHeader("charset","UTF-8")
//                    .addHeader("source-terminal", "Android")     //操作系统名称（注：ios、android）//设备型号
//                    .addHeader("device-model", Build.MODEL)             //设备型号
//                    .addHeader("os-version", Build.VERSION.RELEASE)     //操作系统版本号
//
//            val token = Hawk.get<String>(KEY_TOKEN)
//            if (token != null){
//                builder.addHeader("Authorization", token)
//            }
////            builder.addHeader("lang", getLang())
//            builder.addHeader("deviceId", DEVICE_ID)
//
//            val response = chain.proceed(builder.build())
//            val bytes = response?.body()?.bytes() ?: ""?.toByteArray()
//            val build = response.newBuilder()
//                    .body(ResponseBody.create(MediaType.parse("UTF-8"), bytes)).build()
//            var responseCode = ""
//            var responseMsg = ""
//            try {
//                if (bytes!=null){
//                    val returnData = JsonParser()?.parse(String(bytes))?.asJsonObject
//                    responseCode = returnData?.get("errcode")?.asString?:"10000"
//                    responseMsg = returnData?.get("errmsg")?.asString?:"server error"
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }finally {
//                //交给上层显示错误
//                if(responseCode?.isNotBlank()&&responseCode?.toInt() !=COMMON_SUC_CODE){
//                    ErrorNotice.INSTANCE.notifyError(responseCode?.toInt(),responseMsg)
//                }else {
//
//                }
//            }
//            build
//        }
//
//        //Retrofit实例化
//        retrofit = Retrofit.Builder()
//                .baseUrl(DEBUG_API_BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .client(initClient())
//                .build()
//    }
//
//    /*
//        OKHttp创建
//    */
//    private fun initClient(): OkHttpClient {
//        return OkHttpClient.Builder()
//                .addInterceptor(interceptor) //  这里一定要这个拦截器先添加
//                .addInterceptor(initLogInterceptor())
//                .retryOnConnectionFailure(true)//失败重试一次 比较关键
//                .connectTimeout(10, TimeUnit.SECONDS)
//                .readTimeout(10,TimeUnit.SECONDS)
//                .writeTimeout(10,TimeUnit.SECONDS)
//                .build()
//    }
//
//    /*
//        日志拦截器
//     */
//    private fun initLogInterceptor(): HttpLoggingInterceptor {
//        val interceptor = HttpLoggingInterceptor()
//         if (BuildConfig.DEBUG){
//             interceptor.level = HttpLoggingInterceptor.Level.HEADERS
//             interceptor.level = HttpLoggingInterceptor.Level.BODY
//         } else {
//             interceptor.level =HttpLoggingInterceptor.Level.NONE
//         }
//        return interceptor
//    }
//
//    /*
//        具体服务实例化
//     */
//
//    fun <T> create(service:Class<T>):T{
//        return retrofit.create(service)
//    }
//
//
//}