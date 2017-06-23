package com.zhongmeban.attentionmodle.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.zhongmeban.attentionmodle.activity.ActivityAttentionList;
import com.zhongmeban.attentionmodle.contract.AttentionAddRadiotherapyContract;
import com.zhongmeban.bean.CreateOrDeleteBean;
import com.zhongmeban.bean.postbody.CreateOrUpdateChemotherapyCourseBody;
import com.zhongmeban.bean.postbody.CreateOrUpdateRadiotherapyBody;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.TimeUtils;
import com.zhongmeban.utils.genericity.SPInfo;

import de.greenrobot.dao.attention.Radiotherapy;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Chengbin He on 2017/1/9.
 */

public class AttentionAddRadiotherapyPresenter implements AttentionAddRadiotherapyContract.Presenter {

    public static final int AddOrEditRadiotherapyCodeOk = 200;

    private Activity mContext;
    private AttentionAddRadiotherapyContract.View view;
    private String dosage;//放疗剂量
    private String token;
    private String patientId;
    private long id;
    private String notes;//备注信息
    private int resultType;//放疗结果 1周期结束 2耐受终止 3手术准备 4治疗进行中
    private long startTime;//开始时间
    private String timesCount;//放疗次数
    private String weeksCount;//治疗时长（周）
    private Radiotherapy radiotherapy;//数据库放疗信息
    private boolean ISEDIT;

    public AttentionAddRadiotherapyPresenter(Activity mContext, AttentionAddRadiotherapyContract.View view) {
        this.mContext = mContext;
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        initData();
    }

    private void initData() {
        SharedPreferences sp = mContext.getSharedPreferences(SPInfo.SPUserInfo, Context.MODE_PRIVATE);
        patientId = sp.getString(SPInfo.UserKey_patientId, "");
        token = sp.getString(SPInfo.UserKey_token, "");

        Intent intent = mContext.getIntent();
        radiotherapy = (Radiotherapy) intent.getSerializableExtra(AttentionRadiotherapyDetailPresenter.Radiotherapy);
        if (radiotherapy != null) {
            ISEDIT = true;
            id = radiotherapy.getId();
            startTime = radiotherapy.getStartTime();
            dosage = radiotherapy.getPredictDosage();
            notes = radiotherapy.getNotes();
            weeksCount = radiotherapy.getWeeksCount();
            timesCount = radiotherapy.getCurrentCount();
            resultType = radiotherapy.getResultType();
            initView();
        }
    }

    private void initView() {
        view.setDose(dosage);
        view.setStartTime(TimeUtils.formatTime(startTime));
        view.setWeekCount(weeksCount);
        view.setTimeCount(timesCount);
        view.setResultType(resultType);
        view.setNotes(notes);
    }

    @Override
    public void commitData() {
        CreateOrUpdateRadiotherapyBody body = new CreateOrUpdateRadiotherapyBody();
        body.setId(id);
        body.setPatientId(patientId);
        body.setStartTime(startTime);
        body.setResultType(resultType);

        if (TextUtils.isEmpty(dosage)) {
            dosage = null;
        }
        if (TextUtils.isEmpty(weeksCount)) {
            weeksCount = null;
        }
        if (TextUtils.isEmpty(timesCount)) {
            timesCount = null;
        }
        body.setPredictDosage(dosage);
        body.setWeeksCount(weeksCount);
        body.setCurrentCount(timesCount);
        body.setNotes(notes);

        if (ISEDIT){
            updateRadiotherapyRecord(body);
        }else {
            createRadiotherapyRecord(body);
        }

    }

    @Override
    public void setStartTime(long time) {
        startTime = time;
    }

    @Override
    public void setResultType(int resultType) {
        this.resultType = resultType;
    }

    @Override
    public void setDose(String dose) {
        this.dosage = dose;
    }

    @Override
    public void setWeekCount(String weekCount) {
        this.weeksCount = weekCount;
    }

    @Override
    public void setTimeCount(String daysCount) {
        timesCount = daysCount;
    }

    @Override
    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public boolean canCommit() {
        if (resultType>0&&startTime>0&&!TextUtils.isEmpty(dosage)&&!TextUtils.isEmpty(timesCount)&&!TextUtils.isEmpty(weeksCount)){
            return true;
        }else {
            if (resultType>0&&startTime>0&&!TextUtils.isEmpty(dosage)&&!TextUtils.isEmpty(timesCount)){
                view.showToast("请输入治疗时长");
            }else if (resultType>0&&startTime>0&&!TextUtils.isEmpty(dosage)){
                view.showToast("请输入放疗次数");
            }else if (resultType>0&&startTime>0){
                view.showToast("请输入放疗剂量");
            }else if(resultType>0){
                view.showToast("请输入开始时间");
            }else{
                view.showToast("请选择放疗结果");
            }
            return false;
        }

    }

    /**
     * 创建放疗记录
     *
     * @param body
     */
    private void createRadiotherapyRecord(CreateOrUpdateRadiotherapyBody body) {

        HttpService.getHttpService().postCreateRadiotherapyRecord(body, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CreateOrDeleteBean>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", "createRadiotherapyRecord onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "createRadiotherapyRecord onError");
                        Log.i("hcb", "e" + e);
                        view.dismissProgressDialog();
                        Toast.makeText(mContext, "上传出错，请重新上传", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CreateOrDeleteBean createOrDeleteBean) {
                        Log.i("hcb", "createRadiotherapyRecord onNext");
                        if (createOrDeleteBean.getResult()) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    view.dismissProgressDialog();
                                    Intent intent = new Intent(mContext, ActivityAttentionList.class);
                                    mContext.setResult(AddOrEditRadiotherapyCodeOk, intent);
                                    mContext.finish();
                                }
                            }, 1000);
                        }

                    }
                });
    }

    /**
     * 修改化疗疗程
     * @param body
     */
    private void updateRadiotherapyRecord(CreateOrUpdateRadiotherapyBody body){
        HttpService.getHttpService().postUpdateRadiotherapyRecord(body,token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CreateOrDeleteBean>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb","updateChemotherapyCourseRecord onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb","updateChemotherapyCourseRecord onError");
                        Log.i("hcb","e"+e);
                    }

                    @Override
                    public void onNext(CreateOrDeleteBean createOrDeleteBean) {
                        Log.i("hcb","updateChemotherapyCourseRecord onNext");
                        if (createOrDeleteBean.getResult()) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    view.dismissProgressDialog();
                                    Intent intent = new Intent(mContext, ActivityAttentionList.class);
                                    mContext.setResult(AddOrEditRadiotherapyCodeOk, intent);
                                    mContext.finish();
                                }
                            }, 1000);
                        }
                        }

                });
    }
}
