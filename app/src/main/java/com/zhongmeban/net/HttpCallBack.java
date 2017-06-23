package com.zhongmeban.net;

/**
 * Created by admin on 2016/5/25.
 */
public interface HttpCallBack<T> {

    void success(T response);

    void falied(Exception e);
}
