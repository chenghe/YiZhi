package com.zhongmeban.bean.postbody;

/**
 * Created by HeCheng on 2016/10/4.
 */

public class PauseRadiotherapyBody {

    private int radiotherapyRecordId;//放疗记录id
    private String patientId;//患者Id
    private long startTime;//暂停开始日期
    private String suspendDays;//本次放疗暂停天数
    private String reason;//暂停原因

    public int getRadiotherapyRecordId() {
        return radiotherapyRecordId;
    }

    public void setRadiotherapyRecordId(int radiotherapyRecordId) {
        this.radiotherapyRecordId = radiotherapyRecordId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getSuspendDays() {
        return suspendDays;
    }

    public void setSuspendDays(String suspendDays) {
        this.suspendDays = suspendDays;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
