package com.zhongmeban.base;

import android.content.Context;

/**
 * Created by User on 2016/10/17.
 */

public abstract class BasePresenter {
    protected Context mContext;


    public BasePresenter(Context mContext) {
        this.mContext = mContext;
    }
}
