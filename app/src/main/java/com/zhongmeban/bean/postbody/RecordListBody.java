package com.zhongmeban.bean.postbody;

import java.util.List;

/**
 * Created by Chengbin He on 2016/9/2.
 */
public class RecordListBody extends PageBody{

    private String patientId;
    private List<Integer> recordTypes;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public List<Integer> getRecordTypes() {
        return recordTypes;
    }

    public void setRecordTypes(List<Integer> recordTypes) {
        this.recordTypes = recordTypes;
    }
}
