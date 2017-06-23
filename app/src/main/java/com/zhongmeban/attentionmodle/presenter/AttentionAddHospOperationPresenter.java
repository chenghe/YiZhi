package com.zhongmeban.attentionmodle.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.zhongmeban.attentionmodle.activity.AttentionSurgeryMethordsActivity;
import com.zhongmeban.attentionmodle.contract.AttentionAddHospOperationContract;
import com.zhongmeban.attentionmodle.contract.AttentionAddOperationContract;
import com.zhongmeban.bean.postbody.SurgeryRecordItem;
import com.zhongmeban.utils.TimeUtils;
import com.zhongmeban.utils.genericity.SPInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.attention.SurgeryMethods;
import de.greenrobot.dao.attention.SurgeryRecord;
import de.greenrobot.dao.attention.SurgerySource;

/**
 * 住院新增手术Presenter
 * Created by Chengbin He on 2017/1/6.
 */

public class AttentionAddHospOperationPresenter implements AttentionAddHospOperationContract.Presenter{

    public static final int AddHospOperationOK = 300;

    private Activity mContext;
    private AttentionAddHospOperationContract.View view;
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
    private SurgeryRecordItem surgeryRecordItem;//手术信息
//    private SurgerySource surgerySource;//手术信息
//    private List<SurgeryMethods> surgeryMethodsList = new ArrayList<>();//手术其他信息数据库返回数据
    private boolean ISEDIT = false;


    public AttentionAddHospOperationPresenter(Activity mContext, AttentionAddHospOperationContract.View view) {
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

        //初始化手术信息
        Intent intent = mContext.getIntent();
        surgeryRecordItem = (SurgeryRecordItem) intent.getSerializableExtra(AttentionHospitalizedDetailPresenter.HospSurgerySource);
        chooseMethods = (List<SurgeryMethods>) intent.getSerializableExtra(AttentionHospitalizedDetailPresenter.HospSurgeryMethodsList);
        if (surgeryRecordItem!=null&& !TextUtils.isEmpty(surgeryRecordItem.getHospitalName())){
            ISEDIT = true;
            hospName = surgeryRecordItem.getHospitalName();
            hospId = surgeryRecordItem.getHospitalId();
            initSurgery();
            view.setTitleName("编辑手术信息");
        }else {
            ISEDIT = false;
            hospName = intent.getStringExtra(AttentionAddHospitalizedPresenter.HospName);
            hospId = intent.getIntExtra(AttentionAddHospitalizedPresenter.HospId,0);
            view.setTitleName("新增手术信息");
        }

        view.setHospName(hospName);

    }

    /**
     * 有手术初始化手术
     */
    private void initSurgery(){
        docName = surgeryRecordItem.getDoctorName();
        notes = surgeryRecordItem.getNotes();
        time = surgeryRecordItem.getTime();
        id = surgeryRecordItem.getId();
        therapeuticName = surgeryRecordItem.getTherapeuticName();
        therapeuticId = surgeryRecordItem.getTherapeuticId();

        view.setDoctName(docName);
        view.setNotes(notes);
        view.setTherapeuticName(therapeuticName);
        view.setTime(TimeUtils.formatTime(time));
        if (chooseMethods.size()>0){
            view.setSurgeryMethords(chooseMethods);
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
        this.chooseMethods =chooseMethods;
        Log.i("hcbtest","chooseMethods.size"+this.chooseMethods.size());
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

    @Override
    public Intent backHospitalized(Intent intent) {
        if (surgeryRecordItem == null){
            surgeryRecordItem = new SurgeryRecordItem();
        }
        surgeryRecordItem.setId(id);
        surgeryRecordItem.setHospitalId(hospId);
        surgeryRecordItem.setHospitalName(hospName);
        surgeryRecordItem.setPatientId(patientId);
        surgeryRecordItem.setDoctorName(docName);
        surgeryRecordItem.setTime(time);
        surgeryRecordItem.setTherapeuticName(therapeuticName);
        surgeryRecordItem.setTherapeuticId(therapeuticId);
        surgeryRecordItem.setNotes(notes);

        intent.putExtra(AttentionHospitalizedDetailPresenter.HospSurgerySource,surgeryRecordItem);
        intent.putExtra(AttentionHospitalizedDetailPresenter.HospSurgeryMethodsList, (Serializable) chooseMethods);
        return intent;
    }

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
