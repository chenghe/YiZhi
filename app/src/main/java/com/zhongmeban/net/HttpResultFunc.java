package com.zhongmeban.net;

import com.zhongmeban.bean.HttpResult;
import rx.functions.Func1;

/**
 * Created by User on 2016/11/1.
 */

public class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {
    @Override public T call(HttpResult<T> tHttpResult) {

        if (tHttpResult ==null||tHttpResult.errorCode != 0) {
            throw new ApiException(tHttpResult.errorCode);
        }

        return tHttpResult.data;
    }
}
