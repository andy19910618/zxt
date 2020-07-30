package com.tezwez.base.helper

import android.content.Context
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.tezwez.base.R
import org.jetbrains.anko.runOnUiThread

/**
 * Created by Administrator on 2016/11/20.
 */

object ToastX {

    //多个Toast方法共同使用的变量
    private var pre_Toast_Text: String? = null//先前一个Toast显示的文本（防止重复）
    private var pre_Toast_occur_time: Long = 0//先前一个Toast显示时的时间（记录已显示了多久）

    private fun makeToast(context:Context,my_text: String?, duration: Int = Toast.LENGTH_SHORT, gravity: Int = Gravity.CENTER) {//默认3秒，屏幕居中显示
        context?.runOnUiThread {

            val my_toast = Toast(context)
            val my_view = View.inflate(context, R.layout.toast_view2, null)

            val my_tv_msg = my_view.findViewById<TextView>(R.id.showview)
            my_tv_msg.text = my_text

            my_toast.setGravity(gravity, 0, 150)

            my_toast.view = my_view     //设置中心视图。

            my_toast.duration = duration        //设置持续显示时间。

            my_toast.show()     //弹出显示视图。


        }
    }


    //此处可以加一段：不弹内容相同的Toast的弹出提示的代码
    fun filterSameTextToast(context:Context,text: String?, duration: Int = Toast.LENGTH_SHORT, gravity: Int = Gravity.CENTER) {
        context?.runOnUiThread  {
            //文字不相等的话，直接弹出（不用管时间间隔是否够长）
            if (text != pre_Toast_Text) {
                makeToast(context,text, duration, gravity)
                //并当下记录时间。
                pre_Toast_occur_time = System.currentTimeMillis()
                pre_Toast_Text = text
            } else {
                //文字相等的话
                //如果要显示，需时间间隔大于2000毫秒
                if (System.currentTimeMillis() - pre_Toast_occur_time >= 2000) {
                    makeToast(context,text, duration, gravity)
                    pre_Toast_Text = text
                    pre_Toast_occur_time = System.currentTimeMillis()
                } else {
                    //如果内容一样，且时间间隔小于2000 则什么都不做
                }
            }
            /**
             * 最后记录一下最近弹的内容。
             * 效果和分散到各处记录的  功能是一样的。
             * 已 分散。
             */
        }
    }

    //显示可以自定义时（一般是调短，小于标准：10秒）的Toast
    fun showCustomTimeToast(context:Context,text: String?, millisTime: Long) {
        context?.runOnUiThread  {
            val toast = Toast.makeText(context, text, Toast.LENGTH_LONG)
            toast.show()
            //若干毫秒后，取消Toast显示
            try {
                Handler().postDelayed({
                    toast?.cancel()
                }, millisTime)//millisTime 后，已经关闭掉了Toast
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
