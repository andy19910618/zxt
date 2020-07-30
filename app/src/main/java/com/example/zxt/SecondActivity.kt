package com.example.zxt

import android.os.Bundle
import com.example.zxt.data.api.Utils
import com.orhanobut.hawk.Hawk
import com.tezwez.base.common.BaseActivity
import com.tezwez.base.helper.click
import kotlinx.android.synthetic.main.activity_second.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast

class SecondActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        tvContent.text = "当前ip:$API_BASE_URL"
        tvContentPhoto.text = "当前图片:${Hawk.get<String>("PHOTO")}"
        tvRoomId.text = "当前房间号:${Hawk.get<String>("ROOMID")}"
        etRoomId.setText(Hawk.get<String>("ROOMID") ?: "")
        btnFinish.click {
            if (etIp.text.toString().isNullOrBlank().not() && etIp.text.toString().contains("http://")
                && etPhoto.text.toString().isNullOrBlank().not() && etPhoto.text.toString().contains(
                    "http://")
            ) {
                if (Utils.isChese(etIp.text.toString())) {
                    Hawk.put("IP", etIp.text.toString())
                } else {
                    toast("ip 有中文")
                }

                if (Utils.isChese(etPhoto.text.toString())) {
                    toast("重置成功，重启后生效")
                    Hawk.put("PHOTO", etPhoto.text.toString())
                    finish()
                } else {
                    toast("图片地址 有中文")
                }

            } else {
                toast("输入有误会")
            }
        }

        btnFinish2.click {
            Hawk.put("ROOMID", etRoomId.text.toString())
            finish()
        }




        btnCancle.click {
            finish()
        }
    }
}
