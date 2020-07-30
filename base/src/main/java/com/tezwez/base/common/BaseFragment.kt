package com.tezwez.base.common

import android.media.MediaFormat.KEY_LANGUAGE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orhanobut.hawk.Hawk
import com.tezwez.base.view.ProgressLoading
import kotlinx.android.synthetic.main.head_layout.*
import me.yokeyword.fragmentation.SupportFragment
import java.util.*





abstract class BaseFragment : SupportFragment() {

    private lateinit var mLoadingDialog: ProgressLoading

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLocaleLanguage()
        mLoadingDialog = ProgressLoading.create(_mActivity)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mToolbar?.setNavigationOnClickListener { pop() }
        initView()
        initData()
        initEvent()
    }

    abstract fun getLayoutId(): Int

    abstract fun initView()

    abstract fun initData()

    abstract fun initEvent()

    override fun onDestroy() {
        super.onDestroy()
        hideSoftInput()
    }

    private fun initLocaleLanguage() {
        val resources = this.resources
        val configuration = resources.configuration
        configuration.locale = Hawk.get(KEY_LANGUAGE, Locale.getDefault())
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }


}