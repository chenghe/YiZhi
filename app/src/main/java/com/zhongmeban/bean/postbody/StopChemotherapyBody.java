package com.zhongmeban.bean.postbody;

/**
 * Created by Chengbin He on 2016/8/30.
 */
public class StopChemotherapyBody {

    private int recordId;
    private long endTime;
    private int endReason;
    private String notes;

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getEndReason() {
        return endReason;
    }

    public void setEndReason(int endReason) {
        this.endReason = endReason;
    }

}
