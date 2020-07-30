package com.tezwez.base.easy.listenner;

import android.graphics.Canvas;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;


public class SimpleItemTouchHelper extends ItemTouchHelper.Callback {

    private final ItemTouchCallback mItemTouchCallback;

    public SimpleItemTouchHelper(ItemTouchCallback itemTouchCallback) {
        mItemTouchCallback = itemTouchCallback;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        mItemTouchCallback.onMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mItemTouchCallback.onSwiped(viewHolder.getAdapterPosition());
    }

    //设置滑动item的背景
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        //判断选中状态
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
//            viewHolder.itemView.setBackgroundColor(viewHolder.itemView.getContext().getResources().getColor(R.color.color_EAEAED));
        }
        super.onSelectedChanged(viewHolder, actionState);

    }

    //清除滑动item的背景
    //拖动完成
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // 恢复
//        viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT);

        //防止出现复用问题 而导致条目不显示 方式一
        viewHolder.itemView.setAlpha(1);//1-0
        //设置滑出大小
//            viewHolder.itemView.setScaleX(1);
//            viewHolder.itemView.setScaleY(1);
        mItemTouchCallback.clearView(recyclerView,viewHolder);
        super.clearView(recyclerView, viewHolder);
    }

    //设置滑动时item的背景透明度
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        //dX:水平方向移动的增量(负：往左；正：往右) 0-view.getWidth()
        float alpha = 1 - Math.abs(dX) / viewHolder.itemView.getWidth();
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

            //透明度动画
            viewHolder.itemView.setAlpha(alpha);//1-0
            //设置滑出大小
//            viewHolder.itemView.setScaleX(alpha);
//            viewHolder.itemView.setScaleY(alpha);
        }
//        //防止出现复用问题 而导致条目不显示 方式二
//        if(alpha==0){
//            viewHolder.itemView.setAlpha(1);//1-0
//            //设置滑出大小
////            viewHolder.itemView.setScaleX(1);
////            viewHolder.itemView.setScaleY(1);
//        }
        //此super方法自动处理setTranslationX
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

    }

}
