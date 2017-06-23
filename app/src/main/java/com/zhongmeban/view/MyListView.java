package com.zhongmeban.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Description:
 * user: Chong Chen
 * time：2016/2/23 17:54
 * 邮箱：cchen@ideabinder.com
 */
public class MyListView extends ListView {

    private IXListViewListener mListViewListener;

    public MyListView(Context context) {
        // TODO Auto-generated method stub
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        // TODO Auto-generated method stub
        super(context, attrs);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyle) {
        // TODO Auto-generated method stub
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    public void setXListViewListener(IXListViewListener l) {
        mListViewListener = l;
    }

    public interface IXListViewListener {
        public void onRefresh();

        public void onLoadMore();
    }
}
