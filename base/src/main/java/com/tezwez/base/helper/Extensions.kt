package com.tezwez.base.helper


import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.graphics.Bitmap
import android.media.MediaFormat.KEY_LANGUAGE
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.app.ActivityCompat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.orhanobut.hawk.Hawk
import com.tezwez.base.BuildConfig
import com.tezwez.base.KEY_TOKEN
import com.tezwez.base.R
import com.tezwez.base.common.BaseFragment
import com.tezwez.base.glide.GlideApp
import com.tezwez.base.net.dto.Result
import io.reactivex.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.exceptions.OnErrorNotImplementedException
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.longToast
import retrofit2.HttpException
import java.io.IOException
import java.math.BigDecimal
import java.net.SocketTimeoutException
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * 防止重复点击
 */
var lastTime = 0L

fun View.click(commit: () -> Unit) {

    setOnClickListener {
        if (System.currentTimeMillis() - lastTime < 500) {
            return@setOnClickListener
        }
        lastTime = System.currentTimeMillis()
        commit.invoke()
    }
}

/**
 *  Fragment 中便捷显示 Toast 。
 */
fun BaseFragment.toast(message: Any) {
    if (message is String) {
        this.context?.longToast(message)
    } else if (message is Int) {
        this.context?.longToast(message)
    }
}


fun EditText.isEmpty(): Boolean {
    return this.text.isNullOrEmpty()
}

fun EditText.trimText(): String {
    return this.text.toString().trim()
}


fun ImageView.loadFromUrl(imageUrl: String? = "nothing", placeHolder: Int = R.mipmap.seize_seat) {

    GlideApp.with(this.context)
        .load(imageUrl ?: "nothing")
        .dontAnimate()
        .apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
        .placeholder(placeHolder)
        .error(placeHolder)
        .fitCenter()
        .into(this)
}

fun ImageView.loadFromUrlNoCache(
    imageUrl: String? = "nothing",
    placeHolder: Int = R.mipmap.seize_seat
) {

    GlideApp.with(this.context)
        .load(getGlideUrl(imageUrl ?: "nothing"))
        .skipMemoryCache(false)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .placeholder(placeHolder)
        .error(placeHolder)
        .fitCenter()
        .into(this)
}

/**
 * 显示本地相册图片不需要检查token
 */
fun ImageView.loadFromUrlNoCheck(
    imageUrl: String? = "nothing",
    placeHolder: Int = R.mipmap.seize_seat,
    skipCache: Boolean = false
) {

    var request = GlideApp.with(this.context)
        .load(imageUrl ?: "nothing")
        .skipMemoryCache(skipCache)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(placeHolder)
        .error(placeHolder)
        .fitCenter()

    if (skipCache) {
        request.diskCacheStrategy(DiskCacheStrategy.NONE).into(this)
    } else {
        request.into(this)
    }
//            .into(this)
}

///**
// * 毛玻璃
// */
//fun ImageView.blur(imageUrl: String? = "nothing", placeHolder: Int = R.mipmap.seize_seat) {
//    GlideApp.with(this.context)
//            .load(imageUrl ?: "nothing")
//            .placeholder(placeHolder)
//            .apply(RequestOptions.bitmapTransform(BlurTransformation(1, 100)))
//            .into(this)
//}

///**
// * 圆角
// *
// * redius:传dp值
// */
//fun ImageView.corner(imageUrl: String? = "nothing", redius: Int = 4, placeHolder: Int = R.mipmap.seize_seat) {
//    GlideApp.with(this.context)
//            .load(imageUrl ?: "nothing")
//            .placeholder(placeHolder)
//            .error(placeHolder)
//            .diskCacheStrategy(DiskCacheStrategy.ALL)
//            .apply(RequestOptions.bitmapTransform(RoundedCornersTransformation((Resources.getSystem().displayMetrics.density * redius).toInt(), 0,
//                    RoundedCornersTransformation.CornerType.ALL)))
//            .into(this)
//}

///**
// * 圆角
// *
// * redius:传dp值
// */
//fun ImageView.corner(imageUrl: Int? = 0, redius: Int = 4, placeHolder: Int = R.mipmap.seize_seat) {
//    GlideApp.with(this.context)
//            .load(imageUrl)
//            .placeholder(placeHolder)
//            .error(placeHolder)
//            .diskCacheStrategy(DiskCacheStrategy.ALL)
//            .apply(RequestOptions.bitmapTransform(RoundedCornersTransformation((Resources.getSystem().displayMetrics.density * redius).toInt(), 0,
//                    RoundedCornersTransformation.CornerType.ALL)))
//            .into(this)
//}


/**
 * 圆形图
 */
fun ImageView.circle(imageUrl: String? = "nothing", placeHolder: Int = R.mipmap.seize_seat) {
    GlideApp.with(this.context)
        .load(imageUrl)
        .placeholder(placeHolder)
        .apply(RequestOptions.noTransformation().circleCrop())
        .into(this)
}

//fun getGlideUrl(imageUrl: String?): GlideUrl = GlideUrl(if (BuildConfig.DEBUG) DEBUG_API_BASE_URL + "coron-api/" + imageUrl else DEBUG_API_BASE_URL + "coron-api/" + imageUrl, LazyHeaders.Builder().addHeader("Authorization", Hawk.get<String>(KEY_TOKEN)).build())
fun getGlideUrl(imageUrl: String?): GlideUrl = GlideUrl(
    if (BuildConfig.DEBUG) imageUrl else imageUrl,
    LazyHeaders.Builder().addHeader("Authorization", Hawk.get<String>(KEY_TOKEN)).build()
)


private val onNextStub: (Any) -> Unit = {}
private val onErrorStub: (Throwable) -> Unit =
    { RxJavaPlugins.onError(OnErrorNotImplementedException(it)) }
private val onCompleteStub: () -> Unit = {}


fun <T> applyWidgetSchedulers(windowDuration: Long = 1L): ObservableTransformer<T, T> {
    return ObservableTransformer {
        it.throttleFirst(windowDuration, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
    }
}

fun <T> applyWidgetSchedulersForMilliSeconds(windowDuration: Long = 1L): ObservableTransformer<T, T> {
    return ObservableTransformer {
        it.throttleFirst(windowDuration, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
    }
}

fun <T> applySchedulers(): ObservableTransformer<T, T> {
    return ObservableTransformer {
        it.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}

fun <T> applySchedulersOnFlowable(): FlowableTransformer<T, T> {
    return FlowableTransformer {
        it.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}

fun <T> applySchedulersOnSingle(): SingleTransformer<T, T> {
    return SingleTransformer {
        it.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}

fun <T> T.asSuccess(): Result<T> {
    return Result.Success(this)
}

fun <T> Throwable.asError(): Result<T> {
    return Result.Error(this)
}

fun <T> Single<T>.repToResult(): Observable<Result<T>> {
    return this
        .toObservable()
        .map {
            it.asSuccess()
        }.onErrorReturn {
            when (it) {
                is IOException,
                is HttpException,
                is SocketTimeoutException -> {
                    return@onErrorReturn it.asError()
                }
                else -> {
                    throw it
                }
            }
        }
}


@SuppressLint("MissingPermission")
fun Context.isConnected(): Boolean {
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = cm.activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnected
}

fun Context.getScreenWH(): Array<Int> {
    val dm = resources.displayMetrics
    var heigth = dm.heightPixels
    var width = dm.widthPixels
    return arrayOf(width, heigth)
}

fun Context.setTopTextView(): TextView? {
    if (this is Activity) {
        var text = TextView(this)
        text?.setLineSpacing(1.5f, 1.5f)
        text?.setTextColor(ActivityCompat.getColor(this, android.R.color.holo_red_light))
        val pl = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pl.setMargins(0, 40, 0, 0)
        (window.decorView as FrameLayout).addView(text, pl)
        return text
    }
    return null
}

/*
    扩展Button可用性
 */
fun Button.enable(ckb: CheckBox, method: () -> Boolean) {
    val btn = this
    ckb.setOnCheckedChangeListener { buttonView, isChecked ->
        btn.isEnabled = method()
    }
}

fun Context.needUpdateLocale(pNewUserLocale: Locale): Boolean {
    val _Locale: Locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //7.0有多语言设置获取顶部的语言
        resources.configuration.locales.get(0)
    } else {
        resources.configuration.locale
    }
    return _Locale != pNewUserLocale
}

fun Context.updateLocale(pNewUserLocale: Locale) {
    if (needUpdateLocale(pNewUserLocale)) {
        val _Configuration = resources.configuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            _Configuration.setLocale(pNewUserLocale)
        } else {
            _Configuration.locale = pNewUserLocale
        }
        resources.updateConfiguration(_Configuration, resources.displayMetrics)
    }
}

@SuppressLint("InvalidWakeLockTag")
fun Context.brightScreen() {
    //获取电源管理器对象
    try {
        var pm = this.getSystemService(Context.POWER_SERVICE) as PowerManager
        var mWakeLock = pm.newWakeLock(
            PowerManager.ACQUIRE_CAUSES_WAKEUP or
                    PowerManager.SCREEN_BRIGHT_WAKE_LOCK or
                    PowerManager.ON_AFTER_RELEASE, "SimpleTimer"
        )
        var screenOffTime = 5 * 60 * 1000
        try {
            screenOffTime = Settings.System.getInt(
                contentResolver,
                Settings.System.SCREEN_OFF_TIMEOUT
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mWakeLock.acquire(screenOffTime.toLong())
//        var c = pm.javaClass
//        val wakeUp = c.getMethod("wakeUp", Long::class.java)
//        wakeUp.isAccessible = true
//        wakeUp.invoke(pm, 1000L)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun View.showKeyboard() {
    this.requestFocus()
    val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun Activity.showToast(text: String) {
    runOnUiThread { ToastX.filterSameTextToast(this, text, Toast.LENGTH_SHORT) }
}

fun BigDecimal?.toB(): String {
    return try {
        when {
            this == null -> "0"
            this?.compareTo(BigDecimal.ZERO) == 0 -> "0"
            else -> this?.stripTrailingZeros()?.setScale(
                8,
                BigDecimal.ROUND_HALF_DOWN
            )?.stripTrailingZeros()?.toPlainString() ?: "0"
        }
    } catch (e: Exception) {
        "0"
    }

}

fun BigDecimal?.to4(): String {
    return try {
        if (this == null || this?.compareTo(BigDecimal.ZERO) == 0) "0" else (this?.stripTrailingZeros()?.setScale(
            4,
            BigDecimal.ROUND_HALF_DOWN
        )?.stripTrailingZeros()?.toPlainString() ?: "0")
    } catch (e: Exception) {
        "0"
    }

}

fun BigDecimal?.to(int: Int): String {
    return if (this == null || this?.compareTo(BigDecimal.ZERO) == 0) "0" else (this?.stripTrailingZeros()?.setScale(
        int,
        BigDecimal.ROUND_HALF_DOWN
    )?.toPlainString()
        ?: "0")
}

fun String?.to4(): String {
    try {
        if (this == null || !ValidatorUtil.isNum(this ?: "")) {
            return this ?: ""
        }
        val temp = BigDecimal(this)
        return temp.to4()
    } catch (e: java.lang.Exception) {
        return "0"
    }
}

fun BigDecimal?.to8(): String {
    return if (this == null || this?.compareTo(BigDecimal.ZERO) == 0) "0" else (this?.stripTrailingZeros()?.setScale(
        8,
        BigDecimal.ROUND_HALF_DOWN
    )?.toPlainString()
        ?: "0")
}

fun String?.to8(): String {
    try {

        if (this == null) {
            return "0"
        } else if (!ValidatorUtil.isNum(this ?: "")) {
            return this ?: ""
        }
        val temp = BigDecimal(this)
        return temp.to8()
    } catch (e: java.lang.Exception) {
        return "0"
    }
}


fun <T> List<T>?.getItemOrNull(i: Int): T? {
    return if (this == null) {
        null
    } else {
        if (this.size > i) {
            this[i]
        } else {
            null
        }
    }
}

fun View.hideKeyboard() {
    val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (imm.isActive) {
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}

fun String.beforeZero(): String {
    return if (this.length == 1) "0$this" else this
}

fun getProperty(key: String, defaultValue: String): String {
    var value = defaultValue
    try {
        val c = Class.forName("android.os.SystemProperties")
        val get = c.getMethod("get", String::class.java, String::class.java)
        value = get.invoke(c, key, "unknown") as String
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        return value
    }
}

fun Context.getPackageInfo(packageID: String): ApplicationInfo? {
    return try {
        packageManager.getPackageInfo(packageID, 0).applicationInfo
    } catch (e: Exception) {
        null
    }
}

fun Context.startApp(info: ApplicationInfo?) {
    try {
        val intent = info?.packageName?.let { packageManager.getLaunchIntentForPackage(it) }
        intent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    } catch (e: Exception) {
    }
}

fun Context.startWebApp(url: String) {
    try {
        val intent = Intent()
        intent.action = "android.intent.action.VIEW"
        val contentUrl = Uri.parse(url)
        intent.data = contentUrl
        startActivity(intent)
    } catch (e: Exception) {
    }
}


@SuppressLint("MissingPermission")
fun Context.getPhoneNumber(): String {
    val tm = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    return tm.line1Number//获取本机号码

}

@SuppressLint("MissingPermission")
fun Activity.getDeviceID(): String {
    val tm = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    return tm.getDeviceId()
}

fun Activity.getAppProcessName(): String {
    //当前应用pid
    val pid = android.os.Process.myPid()
    //任务管理类
    val manager = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    //遍历所有应用
    manager.runningAppProcesses?.forEach {
        if (it.pid == pid)//得到当前应用
            return it.processName//返回包名
    }
    return ""
}


/**
 * 是否连接wifi
 */
@SuppressLint("MissingPermission")
fun Context.isConnectWifi(): Boolean {
    val connManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val info = connManager.activeNetworkInfo
    return (info != null && info.isConnected && info.type == ConnectivityManager.TYPE_WIFI)
}

fun String.hide(beforeLength: Int, afterLength: Int): String {
    if (this.isNullOrBlank()) {
        return this
    }
    var tempAfterLength = afterLength
    var tempBeforeLength = beforeLength
    val length = this.length
    //替换字符串，当前使用“*”
    val replaceSymbol = "*"
    val sb = StringBuffer()
    if (this?.contains("@")) {
        tempAfterLength = this?.substring(this?.indexOf("@"), this.length)?.length
        tempBeforeLength = 4
    }
    for (i in 0 until length) {
        if (i < tempBeforeLength || i >= length - tempAfterLength) {
            sb.append(this[i])
        } else {
            if (!sb?.toString()?.contains(replaceSymbol)) {
                sb.append("****")
            }

        }
    }
    return sb.toString()
}


fun String.hideAll(): String {
    return "****"
}

fun String?.toB(): String {
    try {
        if (this == null) {
            return "0"
        } else if (!ValidatorUtil.isNum(this ?: "")) {
            return "0"
        } else if (this?.isNotBlank() == true) {
            return BigDecimal(this).toB()
        } else {
            return "0"
        }
    } catch (e: Exception) {
        return "0"
    }


}

fun Dialog.bottomShow(): Unit {
    this.show()
    val window = this.window
    window!!.setGravity(Gravity.BOTTOM)
    val lp = window.attributes
    val manager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    this?.window?.setWindowAnimations(R.style.DialogAnimBottom)
    val dm = DisplayMetrics()
    manager.defaultDisplay.getMetrics(dm)
    val width = dm.widthPixels
    lp.width = width
    window.attributes = lp

}

inline fun <reified T : ViewGroup.LayoutParams> View.getParams() = this.layoutParams as T

fun View.convertViewToBitmap(): Bitmap {
    this.destroyDrawingCache()
    this.measure(
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    )
    val measuredWidth = this.getMeasuredWidth()
    val measuredHeight = this.getMeasuredHeight()
    this.layout(0, 0, measuredWidth, measuredHeight)
    this.setDrawingCacheEnabled(true)
    return this.getDrawingCache(true)
}

fun Any.getLang(): String {
    val temp = Hawk.get<Locale>(KEY_LANGUAGE, Locale.getDefault())
    when (temp) {
        Locale.CHINA -> {
            return "zh_CN"
        }
        Locale.TAIWAN -> {
            return "zh_TW"
        }
        Locale.ENGLISH -> {
            return "en_US"
        }
        Locale.JAPAN -> {
            return "ja_JP"
        }
        Locale.KOREAN -> {
            return "ko_KR"
        }
        Locale.FRANCE -> {
            return "zh_CN"
        }
        else -> {
            return "zh_CN"
        }
    }
}

fun BaseFragment.isEditsEmpty(vararg edits: EditText): Boolean {
    for (edit in edits) {
        if (edit.isEmpty()) {
            edit.requestFocus()
            // temp toast
            this.toast("内容不能为空")
            return true
        }
    }
    return false
}

