package com.zhongmeban.net;

/**
 * 功能描述：
 * 作者：ysf on 2016/12/12 18:38
 */

import com.zhongmeban.MyApplication;
import com.zhongmeban.bean.HttpResult;
import com.zhongmeban.utils.NetWorkUtils;
import rx.Subscriber;

/**
 * the base response callback entity
 * Created by deadline on 16/8/8.
 *
 * onFinish()方法一定会被调用
 */
public abstract class OnRequestCallback<T extends HttpResult> extends Subscriber<T> {

    public abstract void onFailed(int code, String message);

    public abstract void onException(String message);

    public abstract void onResponse(T response);

    public abstract void onFinish();

    @Override
    public void onStart() {
        if (!NetWorkUtils.isNetworkConnected(MyApplication.app)){
            onFailed( -1, "断wang");
            onFinish();
            unsubscribe();
            return;
        }
    }

    @Override
    public final void onCompleted() {
        onFinish();
    }

    @Override
    public final void onError(Throwable e)
    {
        onException(e.getMessage());
        onFinish();
    }

    @Override
    public final void onNext(T response)
    {
        if(response.getErrorCode()==0)
        {
            onResponse(response);
        }else {
            onFailed(response.getErrorCode(), response.getErrorMessage());
        }
    }
}