package com.zhongmeban.viewinterface;

import com.zhongmeban.base.IBaseView;
import com.zhongmeban.bean.shop.ShopCartQuantity;

/**
 * Created by User on 2016/10/17.
 */

public interface IGetShopCartQuantity extends IBaseView {

    void getShopQuantitySuccess(ShopCartQuantity shopCartQuantity);

    /**
     * 网络请求失败
     */
    void getShopQuantityFail();
}
