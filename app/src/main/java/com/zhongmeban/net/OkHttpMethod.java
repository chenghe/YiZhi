package com.zhongmeban.net;

import com.zhongmeban.bean.BaseBean;
import com.zhongmeban.net.okhttp.OkHttpUtils;
import com.zhongmeban.net.okhttp.callback.GenericsCallback;
import com.zhongmeban.net.okhttp.callback.JsonGenericsSerializator;
import com.zhongmeban.net.okhttp.callback.StringCallback;
import okhttp3.Call;
import okhttp3.MediaType;

/**
 * Created by admin on 2016/5/25.
 */
public class OkHttpMethod {

    public static String BASE_URL = "http://weibo.com/u/3830348248/home?topnav=1&wvr=5";
    public static String URL_STICKER = "plugin/getSticker.do";


    /**
     *
     * @param fragTag
     * @param callBack
     */
    public void getCheckUpdate(String fragTag, final HttpCallBack callBack) {

        OkHttpUtils
            .get()
            .url(BASE_URL)
            .tag(BASE_URL)
            .build()
            .execute(new StringCallback() {
                @Override public void onError(Call call, Exception e, int id) {
                    com.orhanobut.logger.Logger.w(e.toString());
                    callBack.falied(e);
                }


                @Override public void onResponse(String response, int id) {
                    callBack.success(response);
                    com.orhanobut.logger.Logger.v("getCheckUpdate===" + response.toString());
                }
            });

    }


    /**
     *
     * @param fragTag
     * @param callBack
     */
    public void getPost(String fragTag, final HttpCallBack callBack) {

        OkHttpUtils
            .postString()
            .url(BASE_URL + URL_STICKER)
            .tag(BASE_URL)
            .mediaType(MediaType.parse("application/json; charset=utf-8"))
            //.content(new Gson().toJson(new CommonRequestBean(type, classId, APP_ID, CommonUtil.getLanguage(), CommonUtil.getCountry())))
            .build()
            .execute(new GenericsCallback<BaseBean>(new JsonGenericsSerializator()) {
                @Override public void onError(Call call, Exception e, int id) {

                }


                @Override public void onResponse(BaseBean response, int id) {

                }
            });

        //.execute(new StringCallback() {
        //    @Override public void onError(Call call, Exception e, int id) {
        //        com.orhanobut.logger.Logger.w(e.toString());
        //        callBack.falied(e);
        //    }
        //
        //
        //    @Override public void onResponse(String response, int id) {
        //        callBack.success(response);
        //        com.orhanobut.logger.Logger.e(response.toString());
        //    }
        //});

    }


    private static class SingleTonHolder {
        private static final OkHttpMethod INSTANCE = new OkHttpMethod();
    }


    public static OkHttpMethod getInstance() {
        return SingleTonHolder.INSTANCE;
    }

}
