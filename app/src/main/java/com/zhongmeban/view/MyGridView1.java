package com.zhongmeban.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * Description:
 * user: Administrator
 * time：2015/12/26 17:13
 * 邮箱：cchen@ideabinder.com
 */
public class MyGridView1 extends GridView {

    public MyGridView1(Context context) {
        super(context);
    }

    public MyGridView1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridView1(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
