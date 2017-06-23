package com.zhongmeban.bean.postbody;

import java.io.Serializable;
import java.util.List;

/**
 * 创建手术记录 上传用Body
 * Created by Chengbin He on 2016/8/12.
 */
public class CreateSurgeryRecordBody implements Serializable{

    private SurgeryRecordItem surgeryRecord;
    private List<Long> methodIds;

    public SurgeryRecordItem getSurgeryRecord() {
        return surgeryRecord;
    }

    public void setSurgeryRecord(SurgeryRecordItem surgeryRecord) {
        this.surgeryRecord = surgeryRecord;
    }

    public List<Long> getMethodIds() {
        return methodIds;
    }

    public void setMethodIds(List<Long> methodIds) {
        this.methodIds = methodIds;
    }

}
