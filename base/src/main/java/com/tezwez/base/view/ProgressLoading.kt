package com.tezwez.base.view

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import com.tezwez.base.R

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class ProgressLoading private constructor(context: Context, theme: Int) : Dialog(context, theme) {

    companion object {
        private lateinit var mDialog: ProgressLoading

        /*
            创建加载对话框
         */
        fun create(context: Context): ProgressLoading {
            //样式引入
            mDialog = ProgressLoading(context, R.style.LightProgressDialog)
            //设置布局
            mDialog.setContentView(R.layout.progress_dialog)
            mDialog.setCancelable(true)
            mDialog.setCanceledOnTouchOutside(false)
            mDialog.window?.attributes?.gravity = Gravity.CENTER

            val lp = mDialog.window?.attributes
            lp?.dimAmount = 0.2f
            //设置属性
            mDialog.window?.attributes = lp

            return mDialog
        }
    }
    private var timer : Disposable?=null
    /*
        显示加载对话框，动画开始
     */
    fun showLoading() {
        super.show()
    }

    /*
        隐藏加载对话框，动画停止
     */
    fun hideLoading(){
        timer?.dispose()
        timer = Observable.timer(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    super.dismiss()
                }
    }

    /*
       隐藏加载对话框，动画停止
    */
    fun hideLoadingQuick(){
        super.dismiss()
    }
}