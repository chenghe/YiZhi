package com.zhongmeban.viewinterface;

import com.zhongmeban.base.IBaseView;
import com.zhongmeban.bean.shop.ProductBean;

/**
 * Created by User on 2016/10/17.
 */

public interface IGetShopProductHot extends IBaseView {

    void getShopProductHotSuccess(ProductBean result, boolean isLoadMore);

    /**
     * 网络请求失败
     */
    void getShopProductHotFail();
}
