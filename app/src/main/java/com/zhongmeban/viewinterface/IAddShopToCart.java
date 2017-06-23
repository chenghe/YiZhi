package com.zhongmeban.viewinterface;

import com.zhongmeban.base.IBaseView;
import com.zhongmeban.bean.HttpResult;

/**
 * Created by User on 2016/10/17.
 */

public interface IAddShopToCart extends IBaseView {

    void addShopToCartSuccess(HttpResult httpResult);

    /**
     * 网络请求失败
     */
    void addShopToCartFail();
}
