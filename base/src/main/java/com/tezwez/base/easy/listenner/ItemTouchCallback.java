package com.tezwez.base.easy.listenner;


import androidx.recyclerview.widget.RecyclerView;

public interface ItemTouchCallback {
    /**
     * @param fromPosition 起始位置
     * @param toPosition   移动的位置
     */
    void onMove(int fromPosition, int toPosition);

    void onSwiped(int position);

    void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder);//拖动完成
}
