package com.zhongmeban.net;

import com.zhongmeban.address.AddressBean;
import com.zhongmeban.bean.HttpResult;
import com.zhongmeban.bean.shop.AddressListBean;
import com.zhongmeban.bean.shop.FirmOrderBean;
import com.zhongmeban.bean.shop.ProductBean;
import com.zhongmeban.bean.shop.ProductDetailBean;
import com.zhongmeban.bean.shop.PromotionBean;
import com.zhongmeban.bean.shop.ShopCartInfoBean;
import com.zhongmeban.bean.shop.ShopCartQuantity;
import com.zhongmeban.bean.shop.post.AddShopCartBody;
import com.zhongmeban.bean.shop.post.ClearShopCartBody;
import com.zhongmeban.bean.shop.post.CreatAddressBody;
import com.zhongmeban.bean.shop.post.DeleteAddressBody;
import com.zhongmeban.bean.shop.post.DeleteShopCartItemBody;
import com.zhongmeban.bean.shop.post.DeleteShopCartListBody;
import com.zhongmeban.bean.shop.post.UpdateShopCartCheckBody;
import com.zhongmeban.bean.shop.post.UpdateShopCartQuantityBody;
import java.util.List;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by User on 2016/10/20.
 */

public interface ShopService {

    ////////////////////////////////////////////////////////////////////////
    /**
     * 电商模块接口
     */
    String GET_PRODUCT_LIST_URL = "goods/getGoodsPage";//获取首页商品列表
    String GET_PRODUCT_DETAIL_URL = "goods/getGoodsDetail";//获取商品详情
    String GET_ADDRESS = "member/receiver/areaList";//获取商品详情

    String GET_ADDRESS_LIST = "member/receiver/list";//获取用户列地址表
    String POST_CREAT_ADDRESS = "member/receiver/create";//获取用户列地址表
    String POST_UPDATE_ADDRESS = "member/receiver/update";//获取用户列地址表
    String POST_DELETE_ADDRESS = "member/receiver/delete";//获取用户列地址表


    String GET_PEOMOTION = "promotion/content";//获取促销轮播图

    String GET_SHOPCART_LIST = "cart/list";//获取购物车列表
    String GET_SHOPCART_QUANTITY = "cart/quantity";//获取购物车数量

    String POST_SHOPCART_ADD = "cart/add";//增加购物车
    String POST_SHOPCART_UPDATE_QUANTITY = "cart/updateQuantity";//更新购物车数量
    String POST_SHOPCART_UPDATE_CHECK = "cart/updateChecked";//更新购物车是否被选中
    String POST_SHOPCART_DELETE = "cart/delete";//删除购物车摸个
    String POST_SHOPCART_CLEAR = "cart/clear";//清空购物车

    String GET_FIRE_ORDER = "order/checkout";//获取购物车数量order/checkout?memberId=1&cartId=10
    ///////////////////////-------------电商相关-------------/////////////////////////////
    /**
     * 获取治疗方法详情
     */
    //pageNumber  int  可选  1  页码
    //pageSize  int  可选  20  每页显示条数
    //orderType  int  可选  1.置顶降序 2.价格升序 3.价格降序 4.销量降序 5.评分降序 6.日期降序  排序规则
    //isTop  是否热销商品
    @GET(GET_PRODUCT_LIST_URL)
    Observable<HttpResult<ProductBean>> getProductList(
        @Query("pageNumber") int pageNumber, @Query("pageSize") int pageSize,
        @Query("orderType") int orderType, @Query("isTop") boolean isTop);
    @GET(GET_PRODUCT_DETAIL_URL)
    Observable<HttpResult<ProductDetailBean>> getProductDetail(@Query("goodsId") Long goodsId);

    //默认是0获取第一级省市列表
    @GET(GET_ADDRESS)
    Observable<HttpResult<List<AddressBean>>> getAddressInfo(@Query("parent") int parent);
    //默认是1获取用户地址列表，只有一页一共十个
    @Headers("Cache-Control: public, max-age=3600")
    @GET(GET_ADDRESS_LIST)
    Observable<HttpResult<List<AddressListBean>>> getAddressList(
        @Query("pageNumber") int pageNumber, @Query("memberId") Long merId);

    //創建收货地址
    @POST(POST_CREAT_ADDRESS)
    Observable<HttpResult> creatAddress(@Body CreatAddressBody creatAddressBody);

    //修改收货地址
    @POST(POST_UPDATE_ADDRESS)
    Observable<HttpResult<List<AddressListBean>>> updateAddress(@Body CreatAddressBody creatAddressBody);

    //删除收货地址
    @POST(POST_DELETE_ADDRESS)
    Observable<HttpResult<List<AddressListBean>>> deleteAddress(@Body DeleteAddressBody deleteAddressBody);

    //获取商城首页促销轮播信息
    @GET(GET_PEOMOTION)
    Observable<HttpResult<List<PromotionBean>>> getPromotion();

    //获取购物车列表
    @GET(GET_SHOPCART_LIST)
    Observable<HttpResult<ShopCartInfoBean>> getShopCartList(@Query("memberId") Long merId);
    //获取购物车 数量
    @GET(GET_SHOPCART_QUANTITY)
    Observable<HttpResult<ShopCartQuantity>> getShopCartQuantity(@Query("memberId") Long merId);
    //增加购物车
    @POST(POST_SHOPCART_ADD)
    Observable<HttpResult<ShopCartInfoBean>> addShopCart(@Body AddShopCartBody addShopCartBody);

    //修改购物车 某个数量
    @POST(POST_SHOPCART_UPDATE_QUANTITY)
    Observable<HttpResult<ShopCartInfoBean>> updateShopCartQuantity(@Body   UpdateShopCartQuantityBody updateQuantityBody);
    //修改购物车 是否被选中
    @POST(POST_SHOPCART_UPDATE_CHECK)
    Observable<HttpResult<ShopCartInfoBean>> updateShopCartCheck(@Body UpdateShopCartCheckBody updateCheckBody);

    //删除购物车 一个
    @POST(POST_SHOPCART_DELETE)
    Observable<HttpResult<ShopCartInfoBean>> deleteShopCartItem(@Body  DeleteShopCartItemBody deleteBody);
    //修改购物车 多个
    @POST(POST_SHOPCART_DELETE)
    Observable<HttpResult<ShopCartInfoBean>> deleteShopCartList(@Body  DeleteShopCartListBody deleteListBody);
    //清空购物车
    @POST(POST_SHOPCART_CLEAR)
    Observable<HttpResult> clearShopCart(@Body ClearShopCartBody clearBody);
    //确认订单页面
    @GET(GET_FIRE_ORDER)
    Observable<HttpResult<FirmOrderBean>> getFirmOrder(@Query("memberId") Long merId, @Query("cartId") Long cartId);

}
