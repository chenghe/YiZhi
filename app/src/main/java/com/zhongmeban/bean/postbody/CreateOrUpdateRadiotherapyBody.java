package com.zhongmeban.bean.postbody;

import java.util.List;

/**
 * Created by Chengbin He on 2016/8/22.
 */
public class CreateOrUpdateRadiotherapyBody {

    private long id;
    private String patientId;
    private long startTime;
    private String notes;
    private String predictDosage;//放疗剂量
    private String currentCount;//放疗剂量
    private String weeksCount;//放疗剂量
    private int resultType;

    public String getPredictDosage() {
        return predictDosage;
    }

    public void setPredictDosage(String predictDosage) {
        this.predictDosage = predictDosage;
    }

    public String getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(String currentCount) {
        this.currentCount = currentCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getWeeksCount() {
        return weeksCount;
    }

    public void setWeeksCount(String weeksCount) {
        this.weeksCount = weeksCount;
    }

    public int getResultType() {
        return resultType;
    }

    public void setResultType(int resultType) {
        this.resultType = resultType;
    }
}
