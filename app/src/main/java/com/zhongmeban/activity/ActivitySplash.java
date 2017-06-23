package com.zhongmeban.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.zhongmeban.MyApplication;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.bean.HttpResult;
import com.zhongmeban.bean.treatbean.MedicineTypeBean;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.net.OnSimpleRequestCallback;
import com.zhongmeban.net.RxUtil;
import com.zhongmeban.utils.SPUtils;
import com.zhongmeban.utils.ZMBUtil;
import com.zhongmeban.utils.genericity.SPInfo;

import java.util.List;
import rx.functions.Action1;

/**
 * @Description: 启动Splsh页面
 * Created by Chengbin He on 2016/4/18.
 */
public class ActivitySplash extends BaseActivity {
    private String phone;


    @Override protected void initTitle() {
        //友盟：通过代码的方式设置渠道号
        //MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(getApplicationContext(),umengAppkey,channel));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //        setContentView(R.layout.activity_splash);
        //StatusBarCompat.setStatusBarColor(this, getResources().getColor(android.R.color.transparent));

        //        String path = getDiskCacheDir(ActivitySplash.this);

        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        phone = sp.getString("phone", "");

        RxPermissions.getInstance(this)
            .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .subscribe(new Action1<Boolean>() {
                @Override public void call(Boolean aBoolean) {
                    //升级数据库重置ServerTime
                    SharedPreferences dataSp = getSharedPreferences(SPInfo.FILE_NAME,1);
                    int attentionDbVersion = dataSp.getInt(SPInfo.Key_attentionDBVersion,0);
                    Log.i("hcbtest","attentionDbVersion"+attentionDbVersion);
                    if (attentionDbVersion< MyApplication.AttentionSchemaVersion){
                        Log.i("hcbtest","attentionDbVersion< MyApplication.AttentionSchemaVersion");
                        //数据库升级重置
                        SharedPreferences serverTimeSP = getSharedPreferences(SPInfo.SPServerTime,
                                Context.MODE_PRIVATE);
                        SharedPreferences.Editor serverTimeEditor = serverTimeSP.edit();
                        serverTimeEditor.putString(SPInfo.ServerTimeKey_pationListServerTime, "0");
                        serverTimeEditor.putString(SPInfo.ServerTimeKey_markerServerTime, "0");
                        serverTimeEditor.putString(SPInfo.ServerTimeKey_medicineRecordServerTime, "0");
                        serverTimeEditor.putString(SPInfo.ServerTimeKey_surgeryServerTime, "0");
                        serverTimeEditor.putString(SPInfo.ServerTimeKey_hospitalizedServerTime, "0");
                        serverTimeEditor.putString(SPInfo.ServerTimeKey_radiotherapyServerTime, "0");
                        serverTimeEditor.putString(SPInfo.ServerTimeKey_chemotherapyServerTime, "0");
                        serverTimeEditor.putString(SPInfo.ServerTimeKey_chemotherapyCourseServerTime, "0");
                        serverTimeEditor.putString(SPInfo.ServerTimeKey_pationListServerTime, "0");
                        serverTimeEditor.putString(SPInfo.ServerTimeKey_attentionNoticesServerTime, "0");
                        serverTimeEditor.commit();
                        dataSp.edit().putInt(SPInfo.Key_attentionDBVersion,MyApplication.AttentionSchemaVersion);
                    }
                    //不确定是否强制需要存储卡权限，暂时不处理
                    startHome();
                }
            });

    }


    private void startHome() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ActivitySplash.this, ActHome.class);
                Intent intentTest = new Intent(ActivitySplash.this, ActTest.class);
                Intent intentGuide = new Intent(ActivitySplash.this, ActGuide.class);
                //                Intent intent = null;
                //                if (phone.equals("")) {
                //                    intent = new Intent(ActivitySplash.this, ActLoginOrRegister.class);
                //                } else {
                //                    intent = new Intent(ActivitySplash.this, ActHome.class);
                //                }
                //                Intent intent = new Intent(ActivitySplash.this, ActivityAddAttention.class);

                boolean isTest = false;

                String verson = ZMBUtil.getAPPVersionNameFromAPP(ActivitySplash.this);
                String versonKey = verson.substring(0, verson.length() - 2);

                //if (true) {
                if ((Boolean) SPUtils.getParams(ActivitySplash.this, versonKey, true, "")) {
                    startActivity(intentGuide);
                    SPUtils.putApply(ActivitySplash.this, versonKey, false, "");
                } else {
                    startActivity(isTest ? intentTest : intent);
                }

                finish();
            }
        }, 600);
        //test();
    }


    public String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
            || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }


    private void test() {
        HttpService.getHttpService().getMedicineTypeListTest()
            .compose(RxUtil.<HttpResult<List<MedicineTypeBean>>>normalSchedulers())
            .subscribe(new OnSimpleRequestCallback<HttpResult<List<MedicineTypeBean>>>() {
                @Override public void onResponse(HttpResult<List<MedicineTypeBean>> response) {
                    List<MedicineTypeBean> list = response.getData();
                    Logger.d("成功==" + list.toString());
                }
            });

    }
}
