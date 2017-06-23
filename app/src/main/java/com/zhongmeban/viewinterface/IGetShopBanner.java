package com.zhongmeban.viewinterface;

import com.zhongmeban.base.IBaseView;
import com.zhongmeban.bean.shop.PromotionBean;
import java.util.List;

/**
 * Created by User on 2016/10/17.
 */

public interface IGetShopBanner extends IBaseView {

    void getShopBannerSuccess(List<PromotionBean> beanList);

    /**
     * 网络请求失败
     */
    void getShopBannerFail();
}
