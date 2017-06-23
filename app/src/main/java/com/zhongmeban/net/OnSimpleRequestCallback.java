package com.zhongmeban.net;

/**
 * 功能描述：
 * 作者：ysf on 2016/12/12 18:40
 */

import com.orhanobut.logger.Logger;
import com.zhongmeban.bean.HttpResult;

/**
 * 不需要展示进度框的网络请求, 但是出现错误会提示
 * 如果你想使用其他方式展示错误信息, 回调里重写方法即可
 * 该类是abstract的.
 * T.showLong(); 是toast的工具类
 * Created by deadline on 16/8/8.
 */

public abstract class OnSimpleRequestCallback<T extends HttpResult> extends OnRequestCallback<T> {
    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onFinish() {

    };

    @Override
    public void onFailed(int code, String message){
        Logger.e("onFailed--"+message);
    };

    @Override
    public void onException(String message){
        Logger.e("onException--"+message);
    };
}