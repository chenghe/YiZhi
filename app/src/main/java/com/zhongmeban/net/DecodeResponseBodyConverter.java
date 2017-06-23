package com.zhongmeban.net;

import com.google.gson.TypeAdapter;
import com.orhanobut.logger.Logger;
import com.zhongmeban.MyApplication;
import com.zhongmeban.utils.AESOperator;
import com.zhongmeban.utils.SPUtils;
import com.zhongmeban.utils.genericity.SPInfo;
import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by User on 2016/11/21.
 */

public class DecodeResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final TypeAdapter<T> adapter;


    DecodeResponseBodyConverter(TypeAdapter<T> adapter) {
        this.adapter = adapter;
    }


    @Override
    public T convert(ResponseBody value) throws IOException {

        byte[] bytes = value.bytes();

        String result = new String(bytes, "UTF-8");
        String userId = (String) SPUtils.getParams(MyApplication.app, SPInfo.UserKey_userId, "",
            SPInfo.SPUserInfo);
        String zhongmeban = null;

        String mKey = MyApplication.isTest?"":AESOperator.getKey(userId);//如果是测试就把key 设为空，数据原路返回

        try {
            zhongmeban = AESOperator.getInstance().decrypt(result, mKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Logger.i(AESOperator.getKey(userId) + "结果---" + zhongmeban);

        //解密字符串
        return zhongmeban == null ? null : adapter.fromJson(zhongmeban);
    }
}