package com.zhongmeban.bean.postbody;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * 关注 创建 更新 手术Body
 * Created by Chengbin He on 2016/8/16.
 */
public class CreateOrUpdateHospitalRecordBody  implements Serializable{

    private HospitalRecordBody hospitalRecord;
    private List<CreateSurgeryRecordBody> surgeryRecords;

    public HospitalRecordBody getHospitalRecord() {
        return hospitalRecord;
    }

    public void setHospitalRecord(HospitalRecordBody hospitalRecord) {
        this.hospitalRecord = hospitalRecord;
    }

    public List<CreateSurgeryRecordBody> getSurgeryRecords() {
        return surgeryRecords;
    }

    public void setSurgeryRecords(List<CreateSurgeryRecordBody> surgeryRecords) {
        this.surgeryRecords = surgeryRecords;
    }
}
