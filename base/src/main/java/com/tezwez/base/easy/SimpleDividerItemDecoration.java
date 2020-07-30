package com.tezwez.base.easy;

import android.graphics.Rect;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;


public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {


    //获取分割线尺寸
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0, 0, 0, 0);

    }

}
