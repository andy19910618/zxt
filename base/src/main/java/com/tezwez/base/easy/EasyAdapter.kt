package com.be.base.easy


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tezwez.base.R
import com.tezwez.base.easy.listenner.ItemTouchCallback

import java.util.*


/**
 * Created by ben.
 *
 * 简单的RecyclerView适配器
 *
 * 注意：如数据量较大且常变化，请使用BaseAsyncAdapter
 *
 * @see DiffUtil
 * @see EasyAsyncAdapter
 */
open class EasyAdapter<T :Any>(private val layoutResId: Int, private val bindView: (View, Int, T) -> Unit, private var list: List<T> = emptyList()) :
        RecyclerView.Adapter<EasyAdapter.ViewHolder<T>>(), ItemTouchCallback {


    private val mLongClickListener: OnLongClickListener? = null

    interface OnLongClickListener {
        fun onLongClick(item: View, position: Int): Boolean
    }

    override fun onSwiped(position: Int) {
//        this.list.removeAt(position)
//        notifyItemRemoved(position)
    }

    override fun onMove(fromPosition: Int, toPosition: Int) {
        Collections.swap(list, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> {
        val view = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        return ViewHolder(view, bindView)
    }

    /**
     * 更新数据集
     */
    fun submitList(newList: List<T>) {
        val oldList = this.list
        this.list = newList
        notifyDataSetChanged()
//        if (oldList.isEmpty()) {
//            notifyDataSetChanged()
//        } else {
//            DiffUtil.calculateDiff(object : DiffUtil.Callback() {
//                override fun getOldListSize(): Int {
//                    return oldList.size
//                }
//
//                override fun getNewListSize(): Int {
//                    return newList.size
//                }
//
//                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//                    val oldItem = oldList[oldItemPosition]
//                    val newItem = newList[newItemPosition]
//
//                    return oldItem == newItem
//                }
//
//                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//                    val oldItem = oldList[oldItemPosition]
//                    val newItem = newList[newItemPosition]
//
//                    return oldItem == newItem
//                }
//            }, true).dispatchUpdatesTo(this)
//        }
    }

    /**
     * 刷新数据专用,不是刷新数据就是旧数据添加新数据,
     * 外部请求方法可以以页码数是否为1来判断布尔值,传到这里就可以了
     * 若没有页码,直接传true
     */
    fun submitList(newList: List<T>, isRefresh: Boolean) {
        val oldList = this.list
        this.list = newList

        if (isRefresh) {
            notifyDataSetChanged()
        } else {
            DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return oldList.size
                }

                override fun getNewListSize(): Int {
                    return newList.size
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val oldItem = oldList[oldItemPosition]
                    val newItem = newList[newItemPosition]

                    return oldItem == newItem
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val oldItem = oldList[oldItemPosition]
                    val newItem = newList[newItemPosition]

                    return oldItem == newItem
                }
            }, true).dispatchUpdatesTo(this)
        }
    }


    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
        holder.bind(list[position], position)
        //设置点击和长按事件
        if (mLongClickListener != null) {
            holder.itemView.setOnLongClickListener({ mLongClickListener.onLongClick(holder.itemView, position) })
        }
    }

    override fun getItemCount() = list.size

    fun getData() = list

    class ViewHolder<in T>(view: View, private val bindView: (View, Int, T) -> Unit) : RecyclerView.ViewHolder(view) {
        fun bind(item: T, position: Int) {
            with(item) {
                itemView.setTag(R.id.easyadapter_position, position)
                bindView(itemView, position, item)
            }
        }
    }
}