package com.zhongmeban.attentionmodle.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.zhongmeban.attentionmodle.activity.ActivityAttentionList;
import com.zhongmeban.attentionmodle.activity.ActivityAttentionMedicineList;
import com.zhongmeban.attentionmodle.activity.AttentionSurgeryMethordsActivity;
import com.zhongmeban.attentionmodle.contract.AttentionAddOperationContract;
import com.zhongmeban.bean.CreateOrDeleteBean;
import com.zhongmeban.bean.postbody.CreateSurgeryRecordBody;
import com.zhongmeban.bean.postbody.SurgeryRecordItem;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.TimeUtils;
import com.zhongmeban.utils.genericity.SPInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.attention.SurgeryMethods;
import de.greenrobot.dao.attention.SurgerySource;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Chengbin He on 2017/1/4.
 */

public class AttentionAddOperationPresent implements AttentionAddOperationContract.Presenter {

    public static final int AddOrEditOperationCodeOk = 200;

    private Activity mContext;
    private AttentionAddOperationContract.View view;
    private String token;
    private String patientId;
    public String hospName;
    public int hospId;
    private String docName;//医生名称
    private String notes;//备注信息
    private long time;//手术日期
    private int therapeuticId;//手术Id
    private long id;
    private String therapeuticName;//手术名称
    private List<Long> methodIds = new ArrayList<Long>();//选中其他手术项ID，创建用
    private List<SurgeryMethods> chooseMethods = new ArrayList<>();//选中其他手术项
    private SurgerySource surgerySource;//手术信息
    private boolean ISEDIT = false;


    public AttentionAddOperationPresent(Activity mContext, AttentionAddOperationContract.View view) {
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
        if (intent.getSerializableExtra(AttentionOperationDetailPresenter.OPERATION) != null) {
            surgerySource = (SurgerySource) intent.getSerializableExtra(AttentionOperationDetailPresenter.OPERATION);
            ISEDIT = true;
        }

        if (ISEDIT) {
            id = surgerySource.getSurgeryRecord().getId();
            therapeuticId = (int) surgerySource.getSurgeryRecord().getTherapeuticId();
            therapeuticName = surgerySource.getSurgeryRecord().getTherapeuticName();
            time = surgerySource.getSurgeryRecord().getTime();
            hospId = (int) surgerySource.getSurgeryRecord().getHospitalId();
            hospName = surgerySource.getSurgeryRecord().getHospitalName();
            docName = surgerySource.getSurgeryRecord().getDoctorName();
            notes = surgerySource.getSurgeryRecord().getNotes();

            chooseMethods = surgerySource.getSurgeryMethods();
            if (chooseMethods.size() > 0) {
                view.setSurgeryMethords(chooseMethods);
            }
            initView();
            view.setTitleName("编辑手术");
        } else {
            view.setTitleName("新增手术");
        }
    }

    private void initView() {
        view.setNotes(notes);
        view.setDoctName(docName);
        view.setHospName(hospName);
        view.setTherapeuticName(therapeuticName);
        view.setTime(TimeUtils.formatTime(time));
    }

    @Override
    public void commitData() {
        CreateSurgeryRecordBody createSurgeryRecordBody = new CreateSurgeryRecordBody();
        SurgeryRecordItem surgeryRecordItem = new SurgeryRecordItem();
        List<Long> methodIds = new ArrayList<>();
        for (SurgeryMethods surgeryMethods : chooseMethods) {
            methodIds.add(surgeryMethods.getSurgeryMethodId());
        }

        surgeryRecordItem.setId(id);
        surgeryRecordItem.setTherapeuticId(therapeuticId);
        surgeryRecordItem.setTime(time);
        surgeryRecordItem.setPatientId(patientId);
        surgeryRecordItem.setHospitalId(hospId);
        surgeryRecordItem.setNotes(notes);
        surgeryRecordItem.setDoctorName(docName);
        createSurgeryRecordBody.setSurgeryRecord(surgeryRecordItem);
        createSurgeryRecordBody.setMethodIds(methodIds);
        if (ISEDIT) {
            updateSurgeryRecord(createSurgeryRecordBody);
        } else {
            createSurgeryRecord(createSurgeryRecordBody);

        }
    }

    @Override
    public String getHospName() {
        return hospName;
    }

    @Override
    public void setHospName(String name) {
        hospName = name;
    }

    @Override
    public int getHospId() {
        return hospId;
    }

    @Override
    public void setHospId(int id) {
        hospId = id;
    }

    @Override
    public String getTherapeuticName() {
        return therapeuticName;
    }

    @Override
    public void setTherapeuticName(String name) {
        therapeuticName = name;
    }

    @Override
    public int getTherapeuticId() {
        return therapeuticId;
    }

    @Override
    public void setTherapeuticId(int id) {
        therapeuticId = id;
    }

    @Override
    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public void setDoctName(String name) {
        docName = name;
    }

    @Override
    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public void setChooseMethods(List<SurgeryMethods> chooseMethods) {
        this.chooseMethods = chooseMethods;
        Log.i("hcbtest", "chooseMethods.size" + this.chooseMethods.size());
        view.setSurgeryMethords(chooseMethods);
    }

    @Override
    public void removeChooseMethodsPosition(int position) {
        chooseMethods.remove(position);
    }

    @Override
    public Intent chooseMethods(Intent intent) {
        intent.putExtra(AttentionSurgeryMethordsActivity.SurgeryMethodsList, (Serializable) chooseMethods);
        return intent;
    }


    /**
     * 创建手术记录
     */
    private void createSurgeryRecord(CreateSurgeryRecordBody body) {

        HttpService.getHttpService().postCreateSurgeryRecord(body, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CreateOrDeleteBean>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", "createSurgeryRecord onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "createSurgeryRecord onError");
                        Log.i("hcb", "e" + e);
                    }

                    @Override
                    public void onNext(CreateOrDeleteBean createOrDeleteBean) {
                        Log.i("hcb", "createSurgeryRecord onNext");
                        if (createOrDeleteBean.getResult()) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    view.dismissProgressDialog();
                                    Intent intent = new Intent(mContext, ActivityAttentionList.class);
                                    mContext.setResult(AddOrEditOperationCodeOk, intent);
                                    mContext.finish();
                                }
                            }, 1000);
                        }
                    }
                });

    }

    /**
     * 更新手术记录
     */
    private void updateSurgeryRecord(CreateSurgeryRecordBody body) {

        HttpService.getHttpService().postUpdateSurgeryRecord(body, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CreateOrDeleteBean>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", "updateSurgeryRecord onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "updateSurgeryRecord onError");
                        Log.i("hcb", "e" + e);
                    }

                    @Override
                    public void onNext(CreateOrDeleteBean createOrDeleteBean) {
                        Log.i("hcb", "updateSurgeryRecord onNext");
                        if (createOrDeleteBean.getResult()) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    view.dismissProgressDialog();
                                    Intent intent = new Intent(mContext, ActivityAttentionList.class);
                                    mContext.setResult(AddOrEditOperationCodeOk, intent);
                                    mContext.finish();
                                }
                            }, 1000);
                        }
                    }
                });
    }

    /**
     * 判断是否能提交
     *
     * @return
     */
    @Override
    public boolean canCommit() {
        if (hospId > 0 && therapeuticId > 0 && time > 0) {
            return true;
        } else {
            if (hospId > 0 && therapeuticId > 0) {
                view.showToast("请输入手术时间");
            } else if (hospId > 0) {
                view.showToast("请输入手术项目");
            } else {
                view.showToast("请输入治疗医院");
            }
            return false;
        }
    }
}
