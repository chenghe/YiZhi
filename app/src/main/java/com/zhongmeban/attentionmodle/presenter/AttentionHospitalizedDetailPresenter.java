package com.zhongmeban.attentionmodle.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.zhongmeban.MyApplication;
import com.zhongmeban.attentionmodle.activity.ActivityAttentionList;
import com.zhongmeban.attentionmodle.contract.AttentionHospitalizedDeatilContract;
import com.zhongmeban.attentionmodle.contract.AttentionHospitalizedListContract;
import com.zhongmeban.bean.CreateOrDeleteBean;
import com.zhongmeban.bean.HospitalRecordsBean;
import com.zhongmeban.bean.SurgeryRecordsList;
import com.zhongmeban.bean.postbody.DeleteRecordBody;
import com.zhongmeban.net.HttpService;
import com.zhongmeban.utils.AttentionUtils;
import com.zhongmeban.utils.TimeUtils;
import com.zhongmeban.utils.ToastUtil;
import com.zhongmeban.utils.genericity.SPInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.attention.Hospitalized;
import de.greenrobot.dao.attention.HospitalizedDao;
import de.greenrobot.dao.attention.SurgeryMethods;
import de.greenrobot.dao.attention.SurgeryMethodsDao;
import de.greenrobot.dao.attention.SurgeryRecord;
import de.greenrobot.dao.attention.SurgeryRecordDao;
import de.greenrobot.dao.attention.SurgerySource;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Chengbin He on 2016/12/27.
 */

public class AttentionHospitalizedDetailPresenter implements AttentionHospitalizedDeatilContract.Presenter {

    public static final String HOSPITALIZED = "hospitalized";
    public static final String HospSurgerySource = "hospSurgerySource";//住院手术信息
    public static final String HospSurgeryMethodsList = "hospSurgeryMethodsList";//住院手术其他手术项
    public static final int TPYE_SURGERY = 3;//住院类型 手术
    public static final int TPYE_TREATMENT = 2;//住院类型 治疗
    public static final int TPYE_OBSERVE = 1;//住院类型 手术
    public static final int DeleteHospitalizedCodeOk = 300;

    private Activity parentActivity;
    private AttentionHospitalizedDeatilContract.View view;
    private String patientId;
    private String token;
    private Hospitalized hospitalized;//住院数据
    private SurgeryRecord surgeryRecord;//手术数据库返回数据
    private List<SurgeryMethods> surgeryMethodsList = new ArrayList<>();//手术其他信息数据库返回数据

    public AttentionHospitalizedDetailPresenter(Activity parentActivity, AttentionHospitalizedDeatilContract.View view) {
        this.parentActivity = parentActivity;
        this.view = view;
        view.setPresenter(this);
    }


    @Override
    public void start() {
        initData();
    }

    private void initData(){
        Intent intent = parentActivity.getIntent();
        hospitalized = (Hospitalized) intent.getSerializableExtra(HOSPITALIZED);
        SharedPreferences sp = parentActivity.getSharedPreferences(SPInfo.SPUserInfo, Context.MODE_PRIVATE);
        patientId = sp.getString(SPInfo.UserKey_patientId, "");
        token = sp.getString(SPInfo.UserKey_token, "");

        if (hospitalized.getType()==3){
            //入院目的为手术，需要查询手术信息
            getDBSurgeryRecord((int) hospitalized.getId());
            view.surgeryVisable();
            view.setSurgeryDoct(surgeryRecord.getDoctorName());
            view.setSurgery(surgeryRecord.getTherapeuticName());
        }else {
            view.surgeryGone();
        }

        view.setHospName(hospitalized.getHospitalName());
        view.setDoctName(hospitalized.getDoctorName());
        view.setDays(hospitalized.getDayCount());
        view.setHospType(AttentionUtils.getHospitalizedType(hospitalized.getType()));
        if (TextUtils.isEmpty(hospitalized.getDescription())){
            view.setNotes("无");
        }else {
            view.setNotes(hospitalized.getDescription());
        }

        view.setInTime(TimeUtils.changeDateToString(hospitalized.getInTime()));

    }

    @Override
    public void deleteHospitalized() {
        deleteHospitalRecord((int) hospitalized.getId(),token);
    }

    @Override
    public boolean exitSurgery() {
        return hospitalized.getType() == TPYE_SURGERY;
    }

    @Override
    public Intent editHospitalized(Intent intent) {
        intent.putExtra(AttentionHospitalizedDetailPresenter.HOSPITALIZED,hospitalized);
        intent.putExtra(AttentionHospitalizedDetailPresenter.HospSurgerySource,surgeryRecord);
        intent.putExtra(AttentionHospitalizedDetailPresenter.HospSurgeryMethodsList, (Serializable) surgeryMethodsList);
        return intent;
    }


    /**
     * 获取数据库手术信息
     * @param hospitalizedId
     */
    private void getDBSurgeryRecord(int hospitalizedId){
        SurgeryRecordDao surgeryRecordDao = ((MyApplication) parentActivity.getApplication())
                .getAttentionDaoSession().getSurgeryRecordDao();
        SurgeryMethodsDao surgeryMethodsDao = ((MyApplication)parentActivity.getApplication())
                .getAttentionDaoSession().getSurgeryMethodsDao();

        List<SurgeryRecord> surgeryRecordList =  surgeryRecordDao.queryBuilder()
                .where(surgeryRecordDao.queryBuilder().and(SurgeryRecordDao.Properties.HospitalRecordId.eq(hospitalizedId),
                        SurgeryRecordDao.Properties.IsActive.eq(true),
                        SurgeryRecordDao.Properties.PatientId.eq(patientId))).list();
        if (surgeryRecordList.size()>0){
            Log.i("hcb","surgeryRecordList.size()"+surgeryRecordList.size());
            surgeryRecord = surgeryRecordList.get(0);//获取手术信息
            surgeryMethodsList =  surgeryMethodsDao.queryBuilder()
                    .where(SurgeryMethodsDao.Properties.SurgeryRecordId.eq(surgeryRecord.getId())).list();
        }
    }

    /**
     * 删除住院记录
     * @param id
     */
    private void deleteHospitalRecord(int id,String token){
        DeleteRecordBody body = new DeleteRecordBody(id);
        HttpService.getHttpService().postDeleteHospitalRecord(body,token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CreateOrDeleteBean>() {
                    @Override
                    public void onCompleted() {
                        Log.i("hcb","AttentionHospitalizedDetailActivity deleteHospitalRecord onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("hcb","AttentionHospitalizedDetailActivity deleteHospitalRecord onError");
                        Log.i("hcb","e" + e);
                    }

                    @Override
                    public void onNext(CreateOrDeleteBean createOrDeleteBean) {
                        Log.i("hcb","AttentionHospitalizedDetailActivity deleteHospitalRecord onNext");
                        if (createOrDeleteBean.getResult()){
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    view.dismissProgressDialog();
                                    Intent intent = new Intent(parentActivity,ActivityAttentionList.class);
                                    parentActivity.setResult(DeleteHospitalizedCodeOk,intent);
                                    parentActivity.finish();
                                }
                            }, 1000);
                        }
                    }
                });

    }
}
