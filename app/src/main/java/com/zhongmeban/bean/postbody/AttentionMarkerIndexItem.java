package com.zhongmeban.bean.postbody;

/**
 * Created by Chengbin He on 2016/8/1.
 */
public class AttentionMarkerIndexItem {
    private long id;//指标记录Id,更新时必选
    private String patientId;//关注人Id , 必选
    private long hospitalId;//医院Id , 可选
    private int type;//检查目的 , 必选
    private long time;//检查时间 , 必选

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

    public long getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(long hospitalId) {
        this.hospitalId = hospitalId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
