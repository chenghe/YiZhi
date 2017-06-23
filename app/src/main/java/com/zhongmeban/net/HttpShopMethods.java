package com.zhongmeban.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.logger.Logger;
import com.zhongmeban.MyApplication;
import com.zhongmeban.address.AddressBean;
import com.zhongmeban.bean.HttpResult;
import com.zhongmeban.bean.shop.AddressListBean;
import com.zhongmeban.bean.shop.FirmOrderBean;
import com.zhongmeban.bean.shop.ProductBean;
import com.zhongmeban.bean.shop.ProductDetailBean;
import com.zhongmeban.bean.shop.ShopCartInfoBean;
import com.zhongmeban.bean.shop.ShopCartQuantity;
import com.zhongmeban.bean.shop.post.AddShopCartBody;
import com.zhongmeban.bean.shop.post.ClearShopCartBody;
import com.zhongmeban.bean.shop.post.CreatAddressBody;
import com.zhongmeban.bean.shop.post.DeleteAddressBody;
import com.zhongmeban.bean.shop.post.DeleteShopCartListBody;
import com.zhongmeban.bean.shop.post.UpdateShopCartCheckBody;
import com.zhongmeban.bean.shop.post.UpdateShopCartQuantityBody;
import com.zhongmeban.utils.FileHelper;
import com.zhongmeban.utils.NetWorkUtils;
import com.zhongmeban.utils.SPUtils;
import com.zhongmeban.utils.genericity.SPInfo;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by User on 2016/10/20.
 */

public class HttpShopMethods {
    public static final String BASE_URL = "http://10.90.0.44:8060/shopxx/api/";

    private static final int DEFAULT_TIMEOUT = 9000;

    private Retrofit retrofit;
    private ShopService shopService;
    private OkHttpClient client;


    //构造方法私有
    private HttpShopMethods() {

        //设置Log,将会以title 为 OkHttp 打印URL Log
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Logger.e("单利的构造方法商城okhttp==");
        /*client = new OkHttpClient
            .Builder()
            .addInterceptor(interceptor)
            .addInterceptor( provideOfflineCacheInterceptor() )
            .addNetworkInterceptor( provideCacheInterceptor() )
            .cache( provideCache() )
            .build();*/

        File cacheFile = new File(FileHelper.getInstance().getCachePath());

        if (!cacheFile.exists()) {
            cacheFile.mkdirs();
        }

        client = new OkHttpClient
            .Builder()
            .cache(new Cache(cacheFile,
                1024 * 1024 * 100))
            //.cache(new Cache(new File(Environment.getExternalStorageDirectory() + "/zmb", "shop"),
            //    1024 * 1024 * 100))
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
            .addInterceptor(interceptor)
            .addInterceptor(new MInterceptor())
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {

                    String userId = (String) SPUtils.getParams(MyApplication.app,
                        SPInfo.UserKey_userId, "",
                        SPInfo.SPUserInfo);
                    String token = (String) SPUtils.getParams(MyApplication.app,
                        SPInfo.UserKey_token, "",
                        SPInfo.SPUserInfo);
                    Request request = chain.request()
                        .newBuilder()
                        .addHeader("userId", userId)
                        .addHeader("token", token)
                        .addHeader("api-version", HttpService.APIVersion)
                        .build();
                    Logger.v(token+"=当前userid==" + userId);
                    return chain.proceed(request);
                }

            })
            .addNetworkInterceptor(new MInterceptor())
            //.sslSocketFactory(getCertificates())
            .build();

        Gson gson = new GsonBuilder().serializeNulls().create();
        retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(DecodeConverterFactory.create(gson))
            //.addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .client(client)
            .build();
        shopService = retrofit.create(ShopService.class);
    }


    private <T> T creatService(Class<T> clazz) {
        return retrofit.create(clazz);
    }


    public class MInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
            if (NetWorkUtils.isNetworkConnected(MyApplication.app)) {

                Response response = chain.proceed(request);
                String cacheControl = request.cacheControl().toString();

                return response.newBuilder()
                    .removeHeader("Pragma")
                    //.removeHeader("Cache-Control")
                    .header("Cache-Control", "public, " + "max-age=0")
                    .build();

            } else {
                //某些接口不需要做缓存,默认缓存返回都是-1
                int seconds = request.cacheControl().maxAgeSeconds();
                if (seconds == 0) {
                    Response response = chain.proceed(request);
                    return response.newBuilder()
                        .removeHeader("Pragma")
                        //.removeHeader("Cache-Control")
                        .header("Cache-Control", "public, " + "max-age=0")
                        .build();

                } else {
                    int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                    request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
                    Response response = chain.proceed(request);
                    return response.newBuilder()
                        .removeHeader("Pragma")
                        //.removeHeader("Cache-Control")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
                }

            }

            /*return response
                .newBuilder()
                //在这里生成新的响Z应并修改它的响应头
                .header("Cache-Control", "public,max-age=1000")
                .removeHeader("Pragma").build();*/
        }
    }


    public Interceptor provideOfflineCacheInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                /**
                 * 未联网获取缓存数据
                 */
                if (!NetWorkUtils.isNetworkConnected(MyApplication.app)) {
                    //在20秒缓存有效，此处测试用，实际根据需求设置具体缓存有效时间
                    CacheControl cacheControl = new CacheControl.Builder()
                        .maxStale(2000, TimeUnit.SECONDS)
                        .build();

                    request = request.newBuilder()
                        .cacheControl(cacheControl)
                        .build();
                }

                return chain.proceed(request);
            }
        };
    }


    /**
     * 添加证书
     */
    public SSLSocketFactory getCertificates() {
        try {
            InputStream[] certificates = new InputStream[] {
                MyApplication.app.getAssets().open("yizhi.cer") };
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias,
                    certificateFactory.generateCertificate(certificate));

                try {
                    if (certificate != null) {
                        certificate.close();
                    }
                } catch (IOException e) {
                }
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    //设置缓存目录和缓存空间大小
    private Cache provideCache() {
        Cache cache = null;
        try {
            cache = new Cache(new File(MyApplication.app.getCacheDir(), "http-cache"),
                100 * 1024 * 1024); // 10 MB
        } catch (Exception e) {
        }
        return cache;
    }


    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final HttpShopMethods INSTANCE = new HttpShopMethods();
    }


    //获取单例
    public static HttpShopMethods getInstance() {
        return SingletonHolder.INSTANCE;
    }


    /**
     * 有三个地方用到了 client，商城，普通模块，picasso
     */
    public OkHttpClient getClient() {
        return client;
    }


    /**
     * 获取电商首页商品列表
     */
    public void getShopProductList(Subscriber<HttpResult<ProductBean>> subscriber, int pageNumbe, int pageSize, int orderType, boolean isTop, final boolean isLoadMore) {
        shopService.getProductList(pageNumbe, pageSize, orderType, isTop)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(subscriber);

    }


    /**
     * 获取电商首页热门商品列表
     */
    public void getShopProductHot(Subscriber<HttpResult<ProductBean>> subscriber, int pageNumbe, int pageSize, int orderType, boolean isTop, final boolean isLoadMore) {
        shopService.getProductList(pageNumbe, pageSize, orderType, isTop)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(subscriber);

    }


    /**
     * 获取电商商品详情
     */
    public void getProductDetail(Subscriber<HttpResult<ProductDetailBean>> subscriber, Long goodsId) {
        shopService.getProductDetail(goodsId)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(subscriber);

    }


    /**
     * 获取电商商品详情
     */
    public void getAddressInfo(Subscriber<HttpResult<List<AddressBean>>> subscriber, int parent) {
        shopService.getAddressInfo(parent)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(subscriber);

    }


    /**
     * 获取地址列表
     */
    public void getAddressList(int pageNumber, Long memberId, Subscriber<HttpResult<List<AddressListBean>>> subscriber) {
        shopService.getAddressList(pageNumber, memberId)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(subscriber);

    }


    /**
     * 创建地址
     */
    public void creatAddress(CreatAddressBody body, Subscriber<HttpResult> subscriber) {
        shopService.creatAddress(body)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(subscriber);

    }


    /**
     * 更新地址
     */
    public void updateAddress(CreatAddressBody body, Subscriber<HttpResult> subscriber) {
        shopService.updateAddress(body)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(subscriber);

    }


    /**
     * 删除地址
     */
    public void deleteAddress(DeleteAddressBody body, Subscriber<HttpResult> subscriber) {
        shopService.deleteAddress(body)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(subscriber);

    }


    /**
     * 获取商城促销轮播图
     */
    public void getPromotion(Subscriber<HttpResult> subscriber) {
        shopService.getPromotion()
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(subscriber);

    }


    /**
     * 获取购物车列表
     */
    public void getShopCartList(Long memId, Subscriber<HttpResult<ShopCartInfoBean>> subscriber) {
        shopService.getShopCartList(memId)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(subscriber);

    }


    /**
     * 获取购物车数量
     */
    public void getShopCartQuantity(Long memId, Subscriber<HttpResult<ShopCartQuantity>> subscriber) {
        shopService.getShopCartQuantity(memId)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(subscriber);

    }


    /**
     * 增加购物车
     */
    public void addShopCart(AddShopCartBody addShopCartBody, Subscriber<HttpResult<ShopCartInfoBean>> subscriber) {
        shopService.addShopCart(addShopCartBody)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(subscriber);

    }


    /**
     * 更新购物车数量
     */
    public void updateShopCartQuantity(UpdateShopCartQuantityBody updateQuantityBody, Subscriber<HttpResult<ShopCartInfoBean>> subscriber) {
        shopService.updateShopCartQuantity(updateQuantityBody)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(subscriber);

    }


    /**
     * 更新购物车选中
     */
    public void updateShopCartCheck(UpdateShopCartCheckBody updateCheckBody, Subscriber<HttpResult<ShopCartInfoBean>> subscriber) {
        shopService.updateShopCartCheck(updateCheckBody)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(subscriber);

    }


    /**
     * 删除购物车数量
     */
    public void deleteShopCartList(DeleteShopCartListBody deleteListBody, Subscriber<HttpResult<ShopCartInfoBean>> subscriber) {
        shopService.deleteShopCartList(deleteListBody)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(subscriber);

    }


    /**
     * 清空购物车
     */
    public void clearShopCart(ClearShopCartBody clearBody, Subscriber<HttpResult> subscriber) {
        shopService.clearShopCart(clearBody)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(subscriber);

    }


    /**
     * 清空购物车
     */
    public void getCheckOrderInfo(Long memId, Long cartId, Subscriber<HttpResult<FirmOrderBean>> subscriber) {
        shopService.getFirmOrder(memId, cartId)
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(subscriber);

    }

}
