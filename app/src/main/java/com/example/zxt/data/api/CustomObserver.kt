package com.example.zxt.data.api

import androidx.lifecycle.MutableLiveData
import com.example.zxt.App
import com.tezwez.base.helper.isConnected
import com.tezwez.base.helper.loge
import com.tezwez.base.net.dto.Rep
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import org.jetbrains.anko.toast

interface OnFinishedListener<T> {

    fun onSucceed(t: T)

    fun onFailure(throwable: Throwable)
}

/**
 * Base Observer，只对 OnSubscribe() 和 onError() 返回进行处理。
 * 需要子类继承并处理 onSuccess() 才能使用。
 */
abstract class BaseObserver<T> : SingleObserver<T> {

    private var disposable: Disposable? = null

    override fun onSubscribe(d: Disposable) {
        disposable = d
        if (!App.instance().isConnected()) {
            App.instance().toast("网络连接异常！")
            d.dispose()
        }
    }

    override fun onSuccess(t: T) {
        // 根据不同的 errCode 进行处理。
    }

    override fun onError(e: Throwable) {
        // default implementation
//        if (e is NetworkErrorException) {
//
//        } else if (e is SocketException) {
//
//        }
        loge(e.message ?: "")
    }
}

/**
 * Data Observer。
 * 只对 Rep<T> 格式的数据有效，可重写 onSucceed() / onFailure() 对处理进行扩展。
 */
open class DataObserver<T>(private val liveData: MutableLiveData<T>) :
    BaseObserver<Rep<T>>(), OnFinishedListener<T> {

    override fun onSuccess(t: Rep<T>) {
        super.onSuccess(t)

        if (t.isSuccessful()) {
            liveData.value = t.data
            onSucceed(t.data)
        }
    }

    override fun onError(e: Throwable) {
        super.onError(e)
        loge(e.printStackTrace().toString())
        liveData.value = null
        onFailure(e)
    }

    override fun onSucceed(t: T) {
    }

    override fun onFailure(throwable: Throwable) {
    }
}
