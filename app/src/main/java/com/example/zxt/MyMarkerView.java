
package com.example.zxt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
@SuppressLint("ViewConstructor")
public class MyMarkerView extends MarkerView {

    private final TextView tvContent;
    private final TextView num;
    private int mType = 1;

    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        tvContent = findViewById(R.id.tvContent);
        num = findViewById(R.id.num);
    }

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        if (e instanceof CandleEntry) {

            CandleEntry ce = (CandleEntry) e;

            tvContent.setText(Utils.formatNumber(ce.getHigh(), 0, true));
        } else {
            String mFormat = null;
            switch (mType) {
                case 1:
                    mFormat = "日";
                    List<String> list= getDay(7);
                    tvContent.setText((list.get((int) e.getX())).substring((list.get((int) e.getX())).length()-2,(list.get((int) e.getX())).length()) + mFormat);
                    break;
                case 2:
                    mFormat = "日";
                    List<String> list2= getDay(30);
                    tvContent.setText((list2.get((int) e.getX())).substring((list2.get((int) e.getX())).length()-2,(list2.get((int) e.getX())).length()) + mFormat);
                    break;
                case 3:
                    mFormat = "年";
                    if((int) e.getX() < 10){
                        tvContent.setText("0"+((int) e.getX())+ mFormat);
                    }else{
                        tvContent.setText(((int) e.getX())+ mFormat);
                    }
                    break;
                case 4:
                    mFormat = "时";
                    tvContent.setText(((int) e.getX())+ mFormat);
                    break;
                default:
                    mFormat = "日";
                    tvContent.setText(((int) e.getX())+ mFormat);
                    break;
            }

            num.setText(Utils.formatNumber(e.getY(), 0, true));
        }

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }


    public void setType(int type) {
        mType = type;
    }

    public List<String> getDay(int daySum){
        List arrayList = new ArrayList<String>();
        for (int i = 0; i < daySum; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, - i); //向前走一天
            Date date = calendar.getTime();
            arrayList.add(DateUtils.INSTANCE.dateToString(date, DateUtils.INSTANCE.getType1()));
        }
        Collections.reverse(arrayList);
        return arrayList;
    }



}
