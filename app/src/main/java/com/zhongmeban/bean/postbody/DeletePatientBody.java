package com.zhongmeban.bean.postbody;

/**
 * Created by Chengbin He on 2016/9/6.
 */
public class DeletePatientBody {

    private String patientId;

    public DeletePatientBody(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
}
