package com.zhongmeban.bean;

import java.io.Serializable;
import java.util.List;

import de.greenrobot.dao.attention.Hospitalized;
import de.greenrobot.dao.attention.SurgeryMethods;
import de.greenrobot.dao.attention.SurgeryRecord;

/**
 * 关注 住院详情Bean(非上传用)
 * Created by Chengbin He on 2016/10/13.
 */

public class AttentionHospitalizedDetailBean implements Serializable{

    private SurgeryRecord surgeryRecord;//手术信息
    private List<SurgeryMethods> surgeryMethodsList;//手术其他手术项
    private Hospitalized hospitalized;//住院信息

    public AttentionHospitalizedDetailBean(){

    }

    public AttentionHospitalizedDetailBean(SurgeryRecord surgeryRecord, List<SurgeryMethods> surgeryMethodsList, Hospitalized hospitalized) {
        this.surgeryRecord = surgeryRecord;
        this.surgeryMethodsList = surgeryMethodsList;
        this.hospitalized = hospitalized;
    }

    public SurgeryRecord getSurgeryRecord() {
        return surgeryRecord;
    }

    public void setSurgeryRecord(SurgeryRecord surgeryRecord) {
        this.surgeryRecord = surgeryRecord;
    }

    public List<SurgeryMethods> getSurgeryMethodsList() {
        return surgeryMethodsList;
    }

    public void setSurgeryMethodsList(List<SurgeryMethods> surgeryMethodsList) {
        this.surgeryMethodsList = surgeryMethodsList;
    }

    public Hospitalized getHospitalized() {
        return hospitalized;
    }

    public void setHospitalized(Hospitalized hospitalized) {
        this.hospitalized = hospitalized;
    }
}
