package com.zhongmeban.bean.postbody;

/**
 * Created by HeCheng on 2016/10/3.
 */

public class StopRadiotherapyBody {
    private int id;
    private long endTime;
    private String patientId;
    private int status;
    private int endReason;
    private String notes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getEndReason() {
        return endReason;
    }

    public void setEndReason(int endReason) {
        this.endReason = endReason;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
