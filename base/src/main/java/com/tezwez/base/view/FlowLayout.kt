package com.tezwez.base.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import java.util.*

/**
 *
 *Create by Yang on 2019/6/10 14:30
 *
 * Description:流布局
 */
class FlowLayout constructor(context: Context, attrs: AttributeSet? = null) : ViewGroup(context, attrs) {

    private val mLines = ArrayList<Line>()//布局管理里的行
    private var mVerticalSpace = 15//垂直的间隙,用于计算top的总高度

    private var mCurrentLine: Line? = null//记录当前行
    private var mHorizontalSpace = 15//水平的间隙,用于创建新行的时候

    fun setSpace(horizontalSpace: Int, verticalSpace: Int) {
        this.mHorizontalSpace = horizontalSpace
        this.mVerticalSpace = verticalSpace
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        /** 每addView一次就会触发重绘,就会触发ViewGroup的onMeasure方法,至少两次
         *    google把onMeasure定义了:只有当测绘完成后才会调用测绘,在测绘期间请求的测绘都忽略.
         *    addView多次就会运行多次onMeasure方法,会一直添加行,所以每次调用都要清空行
         */

        mLines.clear()
        mCurrentLine = null

        val widthSelf = View.MeasureSpec.getSize(widthMeasureSpec)//拿到条目期望宽
        //child的最大宽度计算:用父类的期望宽-paddingleft-paddingright
        val childMaxWidth = widthSelf - paddingRight - paddingLeft

        /**
         * 测量孩子
         * 1.拿到孩子的总个数
         * 2.变量拿到每个孩子,每个孩子给一个希望值
         * 3.添加进集合
         */
        val count = childCount
        for (i in 0 until count) {
            val child = getChildAt(i)

            //如果孩子不显示,就continue
            if (child.visibility == View.GONE) {
                continue
            }
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            //测一个孩子加一个数据
            //判断如果一行都没有,就new一行
            if (mCurrentLine == null) {

                mCurrentLine = Line(childMaxWidth, mHorizontalSpace)
                //将line添加进布局中
                mLines.add(mCurrentLine!!)
                //将view添加到line中
                mCurrentLine!!.addChild(child)
            } else {
                if (mCurrentLine!!.canAdd(child)) {
                    mCurrentLine!!.addChild(child)
                } else {
                    //因为子控件宽度需要用到,所有把child的最大宽度放到方法外
                    //换行
                    mCurrentLine = Line(childMaxWidth, mHorizontalSpace)
                    //将line添加进布局中
                    mLines.add(mCurrentLine!!)
                    //将view添加到line中
                    mCurrentLine!!.addChild(child)
                }
            }
        }

        /**
         * 测量父类自己的宽高
         * 1.宽就是期望宽
         * 2.高=条目高+条目间的间隔,要判断是不是在第一条,第一条是没有间隔的
         *
         */
        //因为子控件测量需要用到widthSelf,所有放到最上面
        var heightSelf = paddingTop + paddingBottom
        for (i in mLines.indices) {
            val line = mLines[i]//拿到每行
            heightSelf += line.mLineMaxHeight

            //当size为2的时候才走这个方法
            if (i != mLines.size - 1) {
                heightSelf += mVerticalSpace
            }
        }
        setMeasuredDimension(widthSelf, heightSelf)
        //                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

        //测量line.用行来布局,每一行自己管理自己的布局,所以遍历行,创建布局方法(layou()),每行都调用一下
        var top = paddingTop
        for (i in mLines.indices) {
            val line = mLines[i]
            line.layout(paddingLeft, top)

            top += mVerticalSpace + line.mLineMaxHeight//为下一行记录top的值
        }
    }


    private inner class Line(private val mLineMaxWidth: Int//一行中最大的宽度
                             , private val mSpace: Int//控件键的间隙距离
    ) {

        //创建一行中子控件的集合
        private val mViews = ArrayList<View>()

        private var mLineUsedWidth: Int = 0//一行中已经使用的宽度
        var mLineMaxHeight: Int = 0//一行中最大的高度

        /**
         * 用来判断是否可以添加子控件的方法
         * @param view 子控件
         * @return 判断子控件的宽是否大于行的宽
         */
        fun canAdd(view: View): Boolean {
            //如果一个子控件都没有,一定要加进去,不然哪个条目都加不进去
            if (mViews.size == 0) {
                return true
            }

            val childWidth = view.measuredWidth

            return mLineUsedWidth + mSpace + childWidth <= mLineMaxWidth
        }

        /**
         * 添加子控件的方法,在这之前先判断是否可以添加,用canAdd方法
         * @param view 子控件
         */
        fun addChild(view: View) {
            val childWidth = view.measuredWidth//子控件的宽
            val childHeight = view.measuredHeight
            //判断这行中有没有子控件
            if (mViews.size == 0) {
                mLineUsedWidth = childWidth//没有就赋值给使用的宽
                mLineMaxHeight = childHeight
            } else {
                mLineUsedWidth += childWidth + mSpace

                //行的高度决定在子控件中最高的高度
                mLineMaxHeight = if (mLineMaxHeight > childHeight)
                    mLineMaxHeight
                else
                    childHeight
            }
            mViews.add(view)
        }


        /**
         * 给行布局的方法
         * @param left 每个子控件的距离父控件的左边距离
         * @param top 每行的高
         */
        fun layout(left: Int, top: Int) {
            var left = left

            //判断是否有多余的空间
            val surplusWidth = mLineMaxWidth - mLineUsedWidth
            //计算每个子控件获得的平均值
            val avgWidth = (surplusWidth * 1f / mViews.size + 0.5f).toInt()

            for (i in mViews.indices) {
                val child = mViews[i]//获取每一行的子控件
                //有子控件就有期望值
                var childHeight = child.measuredHeight
                var childWidth = child.measuredWidth

                //先不去布局,先期望子控件的宽高,有剩余的多给
                if (avgWidth > 0) {
                    //获得期望值,精确的
                    val childWidthSpec = View.MeasureSpec.makeMeasureSpec(childWidth + avgWidth,
                            View.MeasureSpec.EXACTLY)
                    val childHeightSpec = View.MeasureSpec.makeMeasureSpec(childHeight,
                            View.MeasureSpec.EXACTLY)
                    //期望一下
                    child.measure(childWidthSpec, childHeightSpec)

                    //获得新的宽高
                    childWidth = child.measuredWidth
                    childHeight = child.measuredHeight
                }

                val l = left
                //                int t = top;
                val t = (top.toFloat() + (mLineMaxHeight - childHeight) / 2f + 0.5f).toInt()//这个是让孩子都居中,用于每个孩子大小不一致时
                val r = l + childWidth
                val b = t + childHeight
                child.layout(l, t, r, b)

                left += childWidth + mSpace//为下一个child记录的left值
            }
        }
    }
}