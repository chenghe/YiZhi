package com.zhongmeban.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zhongmeban.MyApplication;
import com.zhongmeban.utils.FileHelper;
import java.io.File;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * @Description: Retrofit 初始化，单例Retrofit对象
 * Created by Chengbin He on 2016/4/18.
 */
public class HttpService {
    //public static final String URL = "http://123.56.182.156:8080/";//阿里云地址
    //    public static final String URL = "http://101.201.48.88:8081/";//阿里云备用地址
    //    public static final String URL = "http://10.90.0.44:8080/";
    //        public static final String URL = "http://59.110.46.0:8080/";

    public static final String URL_DEBUG = "http://101.201.48.88:8082/";//阿里云 1.0.1测试地址

//    public static final String URL_DEBUG = "http://rap.taobao.org/mockjsdata/9606/";//rap 地址
    //    public static final String URL_RELASE = "http://m.yizhi360.com:8080/";//正式线上地址
    public static final String URL_RELASE = "https://m.yizhi360.com/";//https 正式线上地址
    //    public static final String URL_RELASE = "http://m.yizhi360.com:8081/";//正式线上地址
    public static final String URL_BASE = MyApplication.isTest ? URL_DEBUG : URL_RELASE;
    public static final String APIVersion = "3";
    private static HttpUrl httpUrl;

    protected static final Object montior = new Object();


    public static HttpUrl getHttpService() {

        synchronized (montior) {
            if (httpUrl == null) {
                httpUrl = new HttpService().buildRetrofit();
            }
            return httpUrl;
        }
    }


    private HttpService() {
        buildRetrofit();
    }


    private File cacheFile;


    private HttpUrl buildRetrofit() {

        cacheFile = new File(FileHelper.getInstance().getCachePath());
        //if(!cacheFile.exists()){
        //    cacheFile.mkdirs();
        //}

        //设置Log,将会以title 为 OkHttp 打印URL Log
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //        OkHttpClient client = new OkHttpClient.Builder()
        //
        //                .cache(new Cache(cacheFile, 1024 * 1024 * 100))
        //                .addInterceptor(interceptor)
        //                .build();

        //        Logger.v("普通okhttp==" + client.toString());
        Gson gson = new GsonBuilder().serializeNulls().create();
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URL_BASE)
            .addConverterFactory(DecodeConverterFactory.create(gson))
            //.addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .client(HttpShopMethods.getInstance().getClient())
            //.client(client)
            .build();

        httpUrl = retrofit.create(HttpUrl.class);
        return httpUrl;
    }

}
