package com.zhongmeban.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import com.zhongmeban.AdapterClickInterface;
import com.zhongmeban.utils.genericity.ImageUrl;

/**
 * Created by Chengbin He on 2016/3/22.
 */
public abstract class BaseRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public  AdapterClickInterface mClick;//Adapter item被点击时回掉监听
    public String imageUrl = ImageUrl.BaseImageUrl;

    public void setItemClickListener(AdapterClickInterface click) {
        mClick = click;
    }

    public static int dip2px(Context context,float dipValue){
        final float scale=context.getResources().getDisplayMetrics().density;
        return (int)(dipValue*scale+0.5f);
    }

    public static int px2dp(Context context,float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue/scale+0.5f);
    }
}
