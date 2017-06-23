package com.zhongmeban.attentionmodle.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;

import com.zhongmeban.attentionmodle.activity.ActivityAttentionList;
import com.zhongmeban.attentionmodle.contract.AttentionRadiotherapyDetailContract;
import com.zhongmeban.bean.CreateOrDeleteBean;
import com.zhongmeban.bean.postbody.DeleteRecordBody;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.AttentionUtils;
import com.zhongmeban.utils.TimeUtils;
import com.zhongmeban.utils.genericity.SPInfo;

import de.greenrobot.dao.attention.Radiotherapy;
import de.greenrobot.dao.attention.SurgerySource;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 放疗详情Presenter
 * Created by Chengbin He on 2017/1/9.
 */

public class AttentionRadiotherapyDetailPresenter implements AttentionRadiotherapyDetailContract.Presenter {

    public static final String Radiotherapy = "radiotherapy";
    public static final int DeleteRadiotherapyCodeOk = 300;

    private Activity mContext;
    private AttentionRadiotherapyDetailContract.View view;
    private String patientId;
    private String token;
    private Radiotherapy radiotherapy;//数据库放疗信息

    public AttentionRadiotherapyDetailPresenter(Activity mContext, AttentionRadiotherapyDetailContract.View view) {
        this.mContext = mContext;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        initData();
        initView();
    }

    private void initData() {
        SharedPreferences sp = mContext.getSharedPreferences(SPInfo.SPUserInfo, Context.MODE_PRIVATE);
        patientId = sp.getString(SPInfo.UserKey_patientId, "");
        token = sp.getString(SPInfo.UserKey_token, "");

        Intent intent = mContext.getIntent();
        radiotherapy = (Radiotherapy) intent.getSerializableExtra(Radiotherapy);
    }

    private void initView() {
        view.setDose(radiotherapy.getPredictDosage());
        view.setStartTime(TimeUtils.formatTime(radiotherapy.getStartTime()));
        view.setWeeksCount(radiotherapy.getWeeksCount());
        view.setTimesCount(radiotherapy.getCurrentCount());
        view.setNotes(AttentionUtils.getRadiotherapyResultType(radiotherapy.getResultType())+"\n"+radiotherapy.getNotes());
    }

    @Override
    public void deleteRadiotherapy() {
        DeleteRecordBody body = new DeleteRecordBody((int) radiotherapy.getId());
        HttpService.getHttpService().postDeleteRadiotherapy(body,token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CreateOrDeleteBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(CreateOrDeleteBean createOrDeleteBean) {
                        if (createOrDeleteBean.getResult()){
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    view.dismissProgressDialog();
                                    Intent intent = new Intent(mContext,ActivityAttentionList.class);
                                    mContext.setResult(DeleteRadiotherapyCodeOk,intent);
                                    mContext.finish();
                                }
                            }, 1000);
                        }
                    }
                });
    }

    @Override
    public Intent editRadiotherapy(Intent intent) {
        intent.putExtra(Radiotherapy,radiotherapy);
        return intent;
    }


}
