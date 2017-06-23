package com.zhongmeban.shopmodle.presenter;

import android.content.Context;
import com.orhanobut.logger.Logger;
import com.zhongmeban.base.BasePresenter;
import com.zhongmeban.bean.HttpResult;
import com.zhongmeban.bean.shop.ProductBean;
import com.zhongmeban.bean.shop.PromotionBean;
import com.zhongmeban.bean.shop.ShopCartInfoBean;
import com.zhongmeban.bean.shop.ShopCartQuantity;
import com.zhongmeban.bean.shop.post.AddShopCartBody;
import com.zhongmeban.net.HttpShopMethods;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.viewinterface.IAddShopToCart;
import com.zhongmeban.viewinterface.IGetShopBanner;
import com.zhongmeban.viewinterface.IGetShopCartQuantity;
import com.zhongmeban.viewinterface.IGetShopProductHot;
import com.zhongmeban.viewinterface.IGetShopProductList;
import java.util.List;
import rx.Subscriber;

/**
 * Created by Administrator on 2016/9/21 0021.
 */
public class ShopStorePresenter extends BasePresenter {

    public ShopStorePresenter(Context context) {
        super(context);

    }


    /**
     * 获取商城 轮播图
     */

    public void getShopProductList(final IGetShopProductList iGetShopProductList, int pageNumbe, int pageSize, int orderType, boolean isTop, final boolean isLoadMore) {

        HttpShopMethods.getInstance().getShopProductList(new Subscriber<HttpResult<ProductBean>>() {
            @Override public void onCompleted() {

            }


            @Override public void onError(Throwable e) {
                Logger.e("getShopProductList--" + e.toString());
                iGetShopProductList.getShopProductFail();
            }


            @Override public void onNext(HttpResult<ProductBean> productBeanHttpResult) {
                Logger.v("lsit==" + productBeanHttpResult.data.getContent().size());
                iGetShopProductList.getShopProductSuccess(productBeanHttpResult.data, isLoadMore);
            }
        }, pageNumbe, pageSize, orderType, isTop, isLoadMore);

    }


    /**
     * 获取热门
     */

    public void getShopProductHot(final IGetShopProductHot iGetShopProductHot, int pageNumbe, int pageSize, int orderType, boolean isTop, final boolean isLoadMore) {

        HttpShopMethods.getInstance().getShopProductHot(new Subscriber<HttpResult<ProductBean>>() {
            @Override public void onCompleted() {
            }


            @Override public void onError(Throwable e) {

                iGetShopProductHot.getShopProductHotFail();
                Logger.e("getShopProductList--" + e.toString());
            }


            @Override public void onNext(HttpResult<ProductBean> productBeanHttpResult) {
                Logger.v("lsit==" + productBeanHttpResult.data.getContent().size());
                iGetShopProductHot.getShopProductHotSuccess(productBeanHttpResult.data, isLoadMore);
            }
        }, pageNumbe, pageSize, orderType, isTop, isLoadMore);
    }


    /**
     * 获取商城 轮播图
     */
    public void getShopBanner(final IGetShopBanner iGetShopBanner) {
        HttpShopMethods.getInstance().getPromotion(new Subscriber<HttpResult>() {
            @Override public void onCompleted() {

            }


            @Override public void onError(Throwable e) {
                iGetShopBanner.getShopBannerFail();
            }


            @Override public void onNext(HttpResult httpResult) {
                if (httpResult != null && httpResult.errorCode == 0) {

                    iGetShopBanner.getShopBannerSuccess((List<PromotionBean>) httpResult.data);
                }
            }
        });

    }


    /**
     * 获取 购物车数量
     */
    public void getShopCartQuantity(Long memId, final IGetShopCartQuantity iGetShopCartQuantity) {
        HttpShopMethods.getInstance()
            .getShopCartQuantity(memId, new Subscriber<HttpResult<ShopCartQuantity>>() {
                @Override public void onCompleted() {
                }


                @Override public void onError(Throwable e) {
                    iGetShopCartQuantity.getShopQuantityFail();
                }


                @Override public void onNext(HttpResult<ShopCartQuantity> httpResult) {
                    if (httpResult != null && httpResult.errorCode == 0) {

                        iGetShopCartQuantity.getShopQuantitySuccess(httpResult.data);
                    }
                }
            });

    }


    /**
     * 添加购物车
     */
    public void addShopToCart(AddShopCartBody addShopCartBody, final IAddShopToCart iAddShopToCart) {
        HttpShopMethods.getInstance()
            .addShopCart(addShopCartBody, new Subscriber<HttpResult<ShopCartInfoBean>>() {
                @Override public void onCompleted() {
                }
                @Override public void onError(Throwable e) {

                    Logger.e("添加购物车失败---"+e.toString());
                    iAddShopToCart.addShopToCartFail();
                }
                @Override public void onNext(HttpResult<ShopCartInfoBean> httpResult) {

                    Logger.v("添加购物车成功---"+httpResult.toString());
                    if (httpResult != null && httpResult.errorCode == 0) {

                        iAddShopToCart.addShopToCartSuccess(httpResult);
                    }
                }
            });

    }


    private void loadError(Throwable throwable) {
        throwable.printStackTrace();
        ToastUtil.showText(mContext, "网络不见了");
    }

}
