package com.zhongmeban.bean.postbody;

import java.io.Serializable;

/**
 * Created by Chengbin He on 2016/8/16.
 */
public class HospitalRecordBody implements Serializable{

    private long id;
    private int type;//1. 观察 2.治疗 3.手术
    private long inTime;
//    private long outTime;
    private String dayCount;
    private String doctorName;
    private String notes;
    private String patientId;
    private int hospitalId;
//    private int doctorId;


    public String getDayCount() {
        return dayCount;
    }

    public void setDayCount(String dayCount) {
        this.dayCount = dayCount;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getInTime() {
        return inTime;
    }

    public void setInTime(long inTime) {
        this.inTime = inTime;
    }

    public String getDescription() {
        return notes;
    }

    public void setDescription(String notes) {
        this.notes = notes;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }


}
