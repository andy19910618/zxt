package com.example.zxt




import android.content.Intent
import android.os.Bundle
import com.tezwez.base.common.BaseActivity
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class WelcomeActivity : BaseActivity() {



    private lateinit var mDisposable: Disposable
//    private var mCountdownTime = 3


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        initData()

    }

    private fun initData() {
        countdown(2)

    }


    fun countdown(time: Int) {
        var time = time
        if (time < 0) time = 0
        //从0开始发射11个数字为：0-time依次输出，延时0s执行，每1s发射一次。
//        val finalTime = time
        mDisposable = Flowable.intervalRange(0, (time + 1).toLong(), 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { aLong ->

                }
                .doOnComplete {
                    if (!mDisposable.isDisposed) {
                        mDisposable.dispose()
                    }
                    startActivity(Intent(this@WelcomeActivity,MainActivity::class.java))
                    finish()


                }
                .subscribe()
    }


    override fun onDestroy() {
        super.onDestroy()
        if (mDisposable != null && !mDisposable.isDisposed) {
            mDisposable.dispose()
        }

    }
}
