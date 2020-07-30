package com.tezwez.base.view

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.tezwez.base.R
import kotlinx.android.synthetic.main.head_layout.view.*

class HeaderBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    //是否显示"右边"图标
    private var isShowRightImg = false

    private var isShowLeftImg = true
    //左边图片
    private var leftImg = R.mipmap.back

    //右边图片
    private var rightImg = R.mipmap.back
    //Title文字
    private var titleText: String? = null
    //右侧文字
    private var rightText: String? = null
    //背景颜色
    private var bgColor: Int? = null
    //文字颜色
    private var textColor: Int? = null

    init {
        //获取自定义属性
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.HeaderBar)
        isShowLeftImg = typedArray.getBoolean(R.styleable.HeaderBar_isShowLeftImg, true)
        isShowRightImg = typedArray.getBoolean(R.styleable.HeaderBar_isShowRightImg, false)
        leftImg = typedArray.getResourceId(R.styleable.HeaderBar_leftImg, R.mipmap.back)
        rightImg = typedArray.getResourceId(R.styleable.HeaderBar_rightImg, R.mipmap.back)

        titleText = typedArray.getString(R.styleable.HeaderBar_titleText)
        rightText = typedArray.getString(R.styleable.HeaderBar_rightText)
        bgColor =
            typedArray.getColor(R.styleable.HeaderBar_bgColor, resources.getColor(R.color.white))
        textColor =
            typedArray.getColor(
                R.styleable.HeaderBar_textColor,
                resources.getColor(R.color.color_333333)
            )
        initView()
        typedArray.recycle()

    }

    private fun initView() {
        View.inflate(context, R.layout.head_layout, this)
        mToolbar?.setBackgroundColor(bgColor ?: resources.getColor(R.color.white))

        mTitleTv?.setTextColor(textColor ?: resources.getColor(R.color.color_333333))
        mLeftTitle?.setTextColor(textColor ?: resources.getColor(R.color.color_333333))
        mRightTv?.setTextColor(textColor ?: resources.getColor(R.color.color_333333))
        if (isShowLeftImg) {
            mToolbar?.setNavigationIcon(leftImg)
        } else {

        }
        if (isShowRightImg) {
            mRightImg?.setImageResource(rightImg)
            mRightImg.visibility = View.VISIBLE
        } else {
            mRightImg.visibility = View.GONE
        }


        //标题不为空，设置值
        titleText?.let {
            mTitleTv.text = it
        }

        //右侧文字不为空，设置值
        rightText?.let {
            mRightTv.text = it
            mRightTv.visibility = View.VISIBLE
        }

    }

    fun isNotchScreen(activity: Activity) {
        var frame = Rect()
        activity?.window?.decorView?.getWindowVisibleDisplayFrame(frame)
    }

//    fun setBackVisibility(visibility:Int){
//        mBackTv.visibility = visibility
//    }

//    fun setLeftCancelVisibility(visibility:Int){
//        mCancelTv.visibility = visibility
//    }

    fun setRightVisibility(visibility: Int) {
        mRightTv.visibility = visibility
    }

    fun setTitle(text: String) {
        mTitleTv.text = text
    }

    fun setTitle(text: Int) {
        mTitleTv.setText(text)
    }

    fun setRightText(text: String) {
        mRightTv.text = text
    }

    fun setRightText(text: Int) {
        mRightTv.setText(text)
    }

    fun onRightClick(listener: OnClickListener) {
        mRightTv.setOnClickListener(listener)
    }

    fun onRightImgClick(listener: OnClickListener) {
        mRightImg.setOnClickListener(listener)
    }

//    fun onCancelClick(l:OnClickListener){
//        mCancelTv.setOnClickListener(l)
//    }


}