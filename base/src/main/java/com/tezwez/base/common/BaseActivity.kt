package com.tezwez.base.common


import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.head_layout.*
import me.yokeyword.fragmentation.SupportActivity
import me.yokeyword.fragmentation.anim.DefaultNoAnimator
import org.jetbrains.anko.toast


open class BaseActivity : SupportActivity()  {


    //权限
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
                if(this.getResources().getConfiguration().orientation ==Configuration.ORIENTATION_PORTRAIT){
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }

        mToolbar?.setNavigationOnClickListener { finish() }
//        fragmentAnimator = DefaultHorizontalAnimator() //横向动画
//        fragmentAnimator = DefaultVerticalAnimator() //竖向动画
        fragmentAnimator = DefaultNoAnimator() //没有动画
        setStatusBar()
        initLocaleLanguage()
    }

    private fun initLocaleLanguage() {
        val resources = this.getResources()
        val configuration = resources.getConfiguration()
//        configuration.locale = Hawk.get(KEY_LANGUAGE, Locale.getDefault())
//        resources.updateConfiguration(configuration, resources.getDisplayMetrics());//更新配置

    }

    private fun setStatusBar() {
        with(window) {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//            statusBarColor = Color.TRANSPARENT
//            addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }

    override fun onBackPressedSupport() {
        if (exit())return
        super.onBackPressedSupport()

    }

    var exitTime:Long =0
    private fun exit():Boolean{
        if((System.currentTimeMillis()-exitTime)>2000){
            toast("再按一次退出程序")
            exitTime=System.currentTimeMillis()
            return true
        } else{
            pop()
            System.exit(0)
        }
        return false

    }


}