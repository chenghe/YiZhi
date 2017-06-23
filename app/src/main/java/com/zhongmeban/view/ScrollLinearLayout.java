package com.zhongmeban.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.zhongmeban.R;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * SrollView + LinearLayout
 * Created by Chengbin He on 2016/4/28.
 */
public class ScrollLinearLayout extends LinearLayout {



    private int count=1;
    private Context mContext;
    private BaseAdapter adapter;

    public ScrollLinearLayout(Context context) {
        super(context);
        mContext = context;
    }
    public ScrollLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    /**
     * 绑定布局
     */
    public void bindLinearLayout() {
        count = adapter.getCount();
        Log.i("hcb","count is" +count);
        this.removeAllViews();
        for (int i = 0; i < count; i++ ) {
            Log.i("hcb","i is" +i);
            View v = adapter.getView(i, null, this);
//            v.setOnClickListener(this.mClickListener);
            addView(v,i);
        }
    }



    public void setAdapter(BaseAdapter adapter){
        this.adapter = adapter;
        bindLinearLayout();
    }
}
