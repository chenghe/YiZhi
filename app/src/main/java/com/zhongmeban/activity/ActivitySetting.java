package com.zhongmeban.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.zhongmeban.MyApplication;
import com.zhongmeban.R;
import com.zhongmeban.base.BaseActivity;
import com.zhongmeban.bean.CreateOrDeleteBean;
import com.zhongmeban.bean.postbody.UserLogoutBody;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.net.RxUtil;
import com.zhongmeban.utils.FileHelper;
import com.zhongmeban.utils.LayoutActivityTitle;
import com.zhongmeban.utils.SPUtils;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.utils.genericity.SPInfo;
import de.greenrobot.dao.attention.AttentionNoticesDao;
import de.greenrobot.dao.attention.ChemotherapyDao;
import de.greenrobot.dao.attention.ChemotherapyMedicineDao;
import de.greenrobot.dao.attention.HospitalizedDao;
import de.greenrobot.dao.attention.MedicineRecordDao;
import de.greenrobot.dao.attention.PatientDao;
import de.greenrobot.dao.attention.RadiotherapyDao;
import de.greenrobot.dao.attention.RadiotherapySuspendedRecordsDao;
import de.greenrobot.dao.attention.RecordIndexDao;
import de.greenrobot.dao.attention.RecordIndexItemDao;
import de.greenrobot.dao.attention.SurgeryMethodsDao;
import de.greenrobot.dao.attention.SurgeryRecordDao;
import java.io.File;
import org.json.JSONObject;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

/**
 * @Description: 个人页面
 * Created by Chengbin He on 2016/3/23.
 */
public class ActivitySetting extends BaseActivity {

    private LayoutActivityTitle title;
    private Context mContext = ActivitySetting.this;
    private RelativeLayout rl_clean;
    private RelativeLayout rl_feedback;
    private RelativeLayout rl_aboutus;
    private boolean ISLOGINOUT = false;
    private MaterialDialog progressDiaglog;

    private TextView mTvCache;
    private Button bt;//退出登陆
    File cacheFile;
    String token;
    String userId;
    Subscription subscribeCache;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        cacheFile = new File(FileHelper.getInstance().getCachePath());
        initTitle();
        initView();
    }


    private void initView() {
        rl_clean = (RelativeLayout) findViewById(R.id.rl_clean);
        mTvCache = (TextView) findViewById(R.id.tv_cache);
        rl_clean.setOnClickListener(onClickListener);
        rl_feedback = (RelativeLayout) findViewById(R.id.rl_feedback);
        rl_feedback.setOnClickListener(onClickListener);
        rl_aboutus = (RelativeLayout) findViewById(R.id.rl_aboutus);
        rl_aboutus.setOnClickListener(onClickListener);
        bt = (Button) findViewById(R.id.bt_delete);
        bt.setOnClickListener(onClickListener);

        setCacheSize();//设置缓存

        token = (String) SPUtils.getParams(ActivitySetting.this, "token", "",
            SPInfo.SPUserInfo);
        userId = (String) SPUtils.getParams(ActivitySetting.this, "userId", "",
            SPInfo.SPUserInfo);
        if (TextUtils.isEmpty(token)) {
            bt.setVisibility(View.GONE);
        }
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            switch (v.getId()) {
                case R.id.rl_clean:
                    RxPermissions.getInstance(ActivitySetting.this)
                        .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(new Action1<Boolean>() {
                            @Override public void call(Boolean aBoolean) {
                                if (aBoolean){
                                    clearCache();
                                } else{
                                    ToastUtil.showText(ActivitySetting.this,"请先授予存储卡权限");
                                }
                            }
                        });


                    break;
                case R.id.rl_feedback://意见反馈
                    //                    intent.setClass(mContext, ActivityFeedBack.class);
                    //                    startActivity(intent);
                    FeedbackAPI.openFeedbackActivity();
                    FeedbackAPI.setAppExtInfo(new JSONObject());
                    break;
                case R.id.rl_aboutus://关于我们
                    intent.setClass(mContext, ActivityAboutUs.class);
                    startActivity(intent);
                    break;
                case R.id.bt_delete:
                    showDeleteDialog(mContext, new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(
                            @NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            progressDiaglog = showProgressDialog("正在退出", mContext);
                            //确定
                            logOutUser(token, userId);

                            Logger.i("userID " + userId + "--Token--" + token);
                        }
                    }, "是否要退出登录？");
                    break;
            }
        }
    };


    protected void initTitle() {
        title = new LayoutActivityTitle(findViewById(R.id.layout_top));
        title.slideCentertext("设置");
        title.backSlid(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                finish();
            }
        });
    }


    private void setCacheSize() {
        subscribeCache = Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                Logger.v("开始计算--" + FileHelper.getInstance().getCachePath());
                double fileOrFilesSize = FileHelper.getInstance()
                    .getFileOrFilesSize(FileHelper.getInstance().getCachePath(),
                        FileHelper.SIZETYPE_MB);
                if (fileOrFilesSize >= 0) {

                    subscriber.onNext(fileOrFilesSize + " M");
                } else {

                    subscriber.onError(null);
                    subscriber.onNext("获取失败");
                }
                Logger.e("计算大小--" + fileOrFilesSize);
            }
        }).compose(RxUtil.<String>normalSchedulers())
            .subscribe(new Subscriber<String>() {
                @Override
                public void onCompleted() {
                }


                @Override
                public void onError(Throwable e) {
                    mTvCache.setText("获取失败");
                }


                @Override
                public void onNext(String s) {
                    mTvCache.setText(s);
                }
            });

    }

    //    @Override
    //    public void onBackPressed() {
    //        Intent intent = new Intent(mContext,ActHome.class);
    //        intent.putExtra("isLoginOut",ISLOGINOUT);
    //        setResult(200,intent);
    //        finish();
    //    }


    private void clearCache() {

        final ProgressDialog dialog = ProgressDialog.show(this, "", "正在清理···", true, false);
        Observable.create(new Observable.OnSubscribe<Boolean>() {

            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                boolean delete = true;

                if (cacheFile.exists()) {
                    //delete = FileHelper.getInstance()
                    //    .deleteDir(FileHelper.getInstance().getCachePath());
                     FileHelper.getInstance()// 上边注释的方法可能有问题，
                        .DeleteFile(new File(FileHelper.getInstance().getCachePath()));
                }
                subscriber.onNext(delete);
            }
        }).compose(RxUtil.<Boolean>normalSchedulers())
            .subscribe(new Action1<Boolean>() {
                @Override
                public void call(final Boolean aBoolean) {
                    mTvCache.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (dialog != null) dialog.dismiss();

                            Toast.makeText(mContext, aBoolean ? "缓存已清理" : "清理失败",
                                Toast.LENGTH_SHORT).show();
                            if (aBoolean) mTvCache.setText("0.0 M");
                        }
                    }, 1000);

                }
            });

    }


    private void logOutUser(String token, String userId) {
        HttpService.getHttpService()
            .postUserLogOut(new UserLogoutBody(token, userId), token)
            .compose(
                RxUtil.<CreateOrDeleteBean>normalSchedulers())
            .subscribe(new Subscriber<CreateOrDeleteBean>() {
                @Override
                public void onCompleted() {

                }


                @Override
                public void onError(Throwable e) {
                    ToastUtil.showText(ActivitySetting.this, "退出失败");
                    ISLOGINOUT = false;
                    progressDiaglog.dismiss();
                    Logger.e("退出失败--" + e.toString());
                }


                @Override
                public void onNext(CreateOrDeleteBean createOrDeleteBean) {
                    if (createOrDeleteBean.getErrorCode() == 0 ||
                        createOrDeleteBean.getErrorCode() == 2) {
                        clearUserInfo();
                        clearDB();
                        ToastUtil.showText(ActivitySetting.this, "退出成功");
                        bt.setVisibility(View.GONE);
                        ISLOGINOUT = true;
                        if (ISLOGINOUT) {
                            Intent intent = new Intent();
                            intent.putExtra("isLoginOut", ISLOGINOUT);
                            setResult(200, intent);
                            progressDiaglog.dismiss();
                            finish();
                        }
                    } else {
                        ToastUtil.showText(ActivitySetting.this,
                            "退出失败");
                        ISLOGINOUT = false;
                        progressDiaglog.dismiss();
                    }
                }
            });
    }


    private void clearUserInfo() {
        SharedPreferences userSp = getSharedPreferences(SPInfo.SPUserInfo, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSp.edit();
        editor.putString(SPInfo.UserKey_userId, "");
        editor.putString(SPInfo.UserKey_token, "");
        editor.putString(SPInfo.UserKey_phone, "");
        editor.putString(SPInfo.UserKey_patientId, "");
        editor.putString(SPInfo.UserKey_userAvatar, "");
        editor.putString(SPInfo.UserKey_nickname, "");
        editor.putInt(SPInfo.UserKey_patientDiseaseId, 0);
        editor.putInt(SPInfo.UserKey_takingMedicine, 0);
        editor.putInt(SPInfo.UserKey_newNotices, 0);
        editor.putLong(SPInfo.UserKey_recentlyCheck, 0);
        editor.putBoolean(SPInfo.UserKey_UPDATEPATION, true);
        editor.commit();

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

    }


    @Override
    protected void onPause() {
        super.onPause();
        if (subscribeCache != null) subscribeCache.unsubscribe();
    }


    private void clearDB() {
        //标志物名称 表
        RecordIndexDao recordIndexDao = ((MyApplication) getApplication()).getAttentionDaoSession()
            .getRecordIndexDao();
        recordIndexDao.deleteAll();
        //标志物详情 表
        RecordIndexItemDao recordIndexItemDao
            = ((MyApplication) getApplication()).getAttentionDaoSession().getRecordIndexItemDao();
        recordIndexItemDao.deleteAll();
        //      //用药 表
        MedicineRecordDao medicineRecordDao
            = ((MyApplication) getApplication()).getAttentionDaoSession().getMedicineRecordDao();
        medicineRecordDao.deleteAll();
        //手术记录 表
        SurgeryRecordDao surgeryRecordDao
            = ((MyApplication) getApplication()).getAttentionDaoSession().getSurgeryRecordDao();
        surgeryRecordDao.deleteAll();
        //手术其他项目 表
        SurgeryMethodsDao surgeryMethodsDao
            = ((MyApplication) getApplication()).getAttentionDaoSession().getSurgeryMethodsDao();
        surgeryMethodsDao.deleteAll();
        //住院 表
        HospitalizedDao hospitalizedDao
            = ((MyApplication) getApplication()).getAttentionDaoSession().getHospitalizedDao();
        hospitalizedDao.deleteAll();
        //      //放疗 表
        RadiotherapyDao radiotherapyDao
            = ((MyApplication) getApplication()).getAttentionDaoSession().getRadiotherapyDao();
        radiotherapyDao.deleteAll();

        //化疗 表
        ChemotherapyDao chemotherapyDao
            = ((MyApplication) getApplication()).getAttentionDaoSession().getChemotherapyDao();
        chemotherapyDao.deleteAll();
        //化疗 用药 关系表
        ChemotherapyMedicineDao chemotherapyMedicineDao
            = ((MyApplication) getApplication()).getAttentionDaoSession()
            .getChemotherapyMedicineDao();
        chemotherapyMedicineDao.deleteAll();

        //患者表
        PatientDao patientDao = ((MyApplication) getApplication()).getAttentionDaoSession()
            .getPatientDao();
        patientDao.deleteAll();
        //关注提示表
        AttentionNoticesDao attentionNoticesDao
            = ((MyApplication) getApplication()).getAttentionDaoSession().getAttentionNoticesDao();
        attentionNoticesDao.deleteAll();
    }
}
