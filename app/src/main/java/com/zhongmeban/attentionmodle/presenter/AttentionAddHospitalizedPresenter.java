package com.zhongmeban.attentionmodle.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import com.zhongmeban.attentionmodle.activity.ActivityAttentionList;
import com.zhongmeban.attentionmodle.contract.AttentionAddHospitalizedContract;
import com.zhongmeban.bean.CreateOrDeleteBean;
import com.zhongmeban.bean.postbody.CreateOrUpdateHospitalRecordBody;
import com.zhongmeban.bean.postbody.CreateSurgeryRecordBody;
import com.zhongmeban.bean.postbody.HospitalRecordBody;
import com.zhongmeban.bean.postbody.SurgeryRecordItem;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.TimeUtils;
import com.zhongmeban.utils.genericity.SPInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.attention.Hospitalized;
import de.greenrobot.dao.attention.SurgeryMethods;
import de.greenrobot.dao.attention.SurgeryRecord;
import de.greenrobot.dao.attention.SurgerySource;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Chengbin He on 2017/1/3.
 */

public class AttentionAddHospitalizedPresenter implements AttentionAddHospitalizedContract.Presenter {

    public static final int AddOrEditHospitalizedCodeOk = 200;
    public static final String HospName = "hospName";
    public static final String HospId = "hospId";

    private Activity mContext;
    private AttentionAddHospitalizedContract.View view;

    private String patientId;
    private String token;
    private String doctName;//医生姓名
    private String days;//住院天数
    private String hospName;
    private int purposeType;//入院类型 1.观察 2.治疗 3.手术
    private int beforePurposeType;//之前入院类型
    private long inTime;//入院时间
    private int hospId;
    private long id;
    private boolean ISEDITE = false;//是否为编辑标记
    private String notes;
    private Hospitalized hospitalized;//住院数据
    //    private SurgeryRecord surgeryRecord;//手术数据库返回数据
    private List<SurgeryMethods> surgeryMethodsList = new ArrayList<>();//手术其他信息数据库返回数据
    private SurgeryRecordItem surgeryRecordItem = new SurgeryRecordItem();

    public AttentionAddHospitalizedPresenter(Activity mContext, AttentionAddHospitalizedContract.View view) {
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
        if (intent.getSerializableExtra(AttentionHospitalizedDetailPresenter.HOSPITALIZED) != null) {
            hospitalized = (Hospitalized) intent.getSerializableExtra(AttentionHospitalizedDetailPresenter.HOSPITALIZED);
            SurgeryRecord surgeryRecord = (SurgeryRecord) intent.getSerializableExtra(AttentionHospitalizedDetailPresenter.HospSurgerySource);
            surgeryMethodsList = (List<SurgeryMethods>) intent.getSerializableExtra(AttentionHospitalizedDetailPresenter.HospSurgeryMethodsList);
            if (surgeryRecord != null) {
                initSurgery(surgeryRecord);
            }
        }

        if (hospitalized != null) {
            ISEDITE = true;
            view.setTitleName("编辑住院信息");
        } else {
            ISEDITE = false;
            view.setTitleName("新增住院信息");
        }

        if (ISEDITE) {
            doctName = hospitalized.getDoctorName();
            days = hospitalized.getDayCount();
            hospName = hospitalized.getHospitalName();
            hospId = (int) hospitalized.getHospitalId();
            purposeType = hospitalized.getType();
            beforePurposeType = purposeType;
            notes = hospitalized.getDescription();
            inTime = hospitalized.getInTime();
            id = hospitalized.getId();
            initView();
        }
    }

    private void initView() {
        view.setNotes(notes);
        view.setDays(days);
        view.setHospName(hospName);
        view.setDoctName(doctName);
        view.setIntime(TimeUtils.formatTime(inTime));
        view.setPurposeType(purposeType);
        if (purposeType == 3) {
            view.setSurgeryName(surgeryRecordItem.getTherapeuticName());
        }
    }

    /**
     * 初始化住院信息
     */
    private void initSurgery(SurgeryRecord surgeryRecord) {

        surgeryRecordItem.setTime(surgeryRecord.getTime());
        surgeryRecordItem.setTherapeuticId((int) surgeryRecord.getTherapeuticId());
        surgeryRecordItem.setHospitalId((int) surgeryRecord.getHospitalId());
        surgeryRecordItem.setPatientId(surgeryRecord.getPatientId());
        surgeryRecordItem.setDoctorName(surgeryRecord.getDoctorName());
        surgeryRecordItem.setNotes(surgeryRecord.getNotes());
        surgeryRecordItem.setId(surgeryRecord.getId());
        surgeryRecordItem.setHospitalName(surgeryRecord.getHospitalName());
        surgeryRecordItem.setTherapeuticName(surgeryRecord.getTherapeuticName());
    }


    @Override
    public void commitData() {
        CreateOrUpdateHospitalRecordBody createOrUpdateHospitalRecordBody = new CreateOrUpdateHospitalRecordBody();
        HospitalRecordBody hospitalRecordBody = new HospitalRecordBody();
        List<CreateSurgeryRecordBody> surgeryList = new ArrayList<>();

        hospitalRecordBody.setId(id);
        hospitalRecordBody.setType(purposeType);
        hospitalRecordBody.setInTime(inTime);
        hospitalRecordBody.setDayCount(days);
        hospitalRecordBody.setDoctorName(doctName);
        hospitalRecordBody.setDescription(notes);
        hospitalRecordBody.setPatientId(patientId);
        hospitalRecordBody.setHospitalId(hospId);

        createOrUpdateHospitalRecordBody.setHospitalRecord(hospitalRecordBody);
        if (purposeType == 3) {
            CreateSurgeryRecordBody createSurgeryRecordBody = new CreateSurgeryRecordBody();
            createSurgeryRecordBody.setSurgeryRecord(surgeryRecordItem);
            if (surgeryMethodsList.size() > 0) {
                List<Long> list = new ArrayList<>();
                for (SurgeryMethods surgeryMethods : surgeryMethodsList) {
                    list.add(surgeryMethods.getSurgeryMethodId());
                }
                createSurgeryRecordBody.setMethodIds(list);
            }

            surgeryList.add(createSurgeryRecordBody);
            createOrUpdateHospitalRecordBody.setSurgeryRecords(surgeryList);
        }


        if (ISEDITE) {
            updateHospitalRecord(createOrUpdateHospitalRecordBody);
        } else {
            createHospitalRecord(createOrUpdateHospitalRecordBody);
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
    public void setIntime(long time) {
        inTime = time;
    }

    @Override
    public void setDays(String days) {
        this.days = days;
    }

    public void setDoctName(String name) {
        this.doctName = name;
    }

    @Override
    public void setPurposeType(int type) {
        purposeType = type;
    }

    @Override
    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public Intent addSurgery(Intent intent) {

        intent.putExtra(HospName, hospName);
        intent.putExtra(HospId, hospId);
        intent.putExtra(AttentionHospitalizedDetailPresenter.HospSurgerySource, surgeryRecordItem);
        intent.putExtra(AttentionHospitalizedDetailPresenter.HospSurgeryMethodsList, (Serializable) surgeryMethodsList);
        return intent;
    }


    @Override
    public void getAddHospOperationResultData(Intent data) {
        surgeryRecordItem = (SurgeryRecordItem) data.getSerializableExtra(AttentionHospitalizedDetailPresenter.HospSurgerySource);
        List<SurgeryMethods> list = (List<SurgeryMethods>) data.getSerializableExtra(AttentionHospitalizedDetailPresenter.HospSurgeryMethodsList);
        if (list != null) {
            surgeryMethodsList.clear();
            surgeryMethodsList.addAll(list);
        }
        view.setSurgeryName(surgeryRecordItem.getTherapeuticName());
    }


    /**
     * 创建住院记录
     */
    private void createHospitalRecord(CreateOrUpdateHospitalRecordBody body) {
        HttpService.getHttpService().postCreateHospitalRecord(body, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CreateOrDeleteBean>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", "createHospitalRecord onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "createHospitalRecord onError");
                        Log.i("hcb", "e" + e);
                    }

                    @Override
                    public void onNext(CreateOrDeleteBean createOrDeleteBean) {
                        Log.i("hcb", "createHospitalRecord onNext");
                        if (createOrDeleteBean.getResult()) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    view.dismissProgressDialog();
                                    Intent intent = new Intent(mContext, ActivityAttentionList.class);
                                    mContext.setResult(AddOrEditHospitalizedCodeOk, intent);
                                    mContext.finish();
                                }
                            }, 1000);
                        }
                    }
                });
    }

    /**
     * //     *更新住院记录
     * //
     */
    private void updateHospitalRecord(CreateOrUpdateHospitalRecordBody body) {
        HttpService.getHttpService().postUpdateHospitalRecord(body, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CreateOrDeleteBean>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb", "updateHospitalRecord onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb", "updateHospitalRecord onError");
                        Log.i("hcb", "e" + e);
                    }

                    @Override
                    public void onNext(CreateOrDeleteBean createOrDeleteBean) {
                        Log.i("hcb", "updateHospitalRecord onNext");
                        if (createOrDeleteBean.getResult()) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    view.dismissProgressDialog();
                                    Intent intent = new Intent(mContext, ActivityAttentionList.class);
                                    mContext.setResult(AddOrEditHospitalizedCodeOk, intent);
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
        if (purposeType > 0 && hospId > 0 && inTime > 0) {
            if (purposeType==3&&surgeryRecordItem.getTherapeuticId()<=0){
                view.showToast("请选择手术");
                return false;
            }
            return true;
        } else {
            if (purposeType > 0 && hospId > 0) {
                view.showToast("请输入入院时间");
            } else if (purposeType > 0) {
                view.showToast("请输入治疗医院");
            } else {
                view.showToast("请输入入院目的");
            }
            return false;
        }
    }

    @Override
    public boolean changePurposeType(){
        Log.i("hcbtext","ISEDITE&& (beforePurposeType==3) && (beforePurposeType!=purposeType)" +ISEDITE +beforePurposeType +purposeType);
        if (ISEDITE&& (beforePurposeType==3) && (beforePurposeType!=purposeType)){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public int getBeforePurposeType() {
        return beforePurposeType;
    }

    @Override
    public String getTherapeuticName() {
        return surgeryRecordItem.getTherapeuticName();
    }
}
