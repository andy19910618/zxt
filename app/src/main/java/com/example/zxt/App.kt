package com.example.zxt

import android.app.Application
import com.orhanobut.hawk.Hawk
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.tezwez.club.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import kotlin.properties.Delegates

/**
 * Description : App
 * Author      : jovelin
 * Version     : 2.0
 * Create Date : 2019-09-15 11:15
 */
class App : Application() {

    companion object {
        private var instance: App by Delegates.notNull()
        fun instance() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Hawk.init(this).build()
        initInjector()
        initBugly()
    }

    private fun initInjector() {
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(
                appModule
            ))
        }
    }


    private fun initBugly() {

        /**
         * 只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗;
         * 不设置会默认所有activity都可以显示弹窗;
         */
        Beta.canShowUpgradeActs.add(MainActivity::class.java)
        /**
         * true表示初始化时自动检查升级
         * false表示不会自动检查升级，需要手动调用Beta.checkUpgrade()方法
         */
        Beta.autoCheckUpgrade = true//设置自动检查
        //如果你不想在通知栏显示下载进度，你可以将这个接口设置为false，默认值为true。  为什么置为false.是因为8.0系统,通知栏要适配
        Beta.enableNotification = false
        /**
         * 点击过确认的弹窗在APP下次启动自动检查更新时会再次显示;60s之内不会重复
         */
        Beta.showInterruptedStrategy = true
        /**Bugly管理平台：https://bugly.qq.com/v2/ */

//        Bugly.init(this, if (BuildConfig.DEBUG)"26ba840d38" else "ea43105868", !BuildConfig.DEBUG)
        Bugly.init(this, "143e465a0d", true)


    }


}