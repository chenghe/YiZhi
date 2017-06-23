package com.zhongmeban.bean.treatbean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Chengbin He on 2016/12/8.
 */

public class TreatDoctor implements Serializable{
    private int doctorId;
    private String doctorName;
    private String avatar;//缺失数据
    private String hospitalName;
    private String pinyinName;
    private int title;
    private List<String> diseaseNames;//返回的疾病是对象
    public int viewType = 1;//0为上拉刷新界面，1为数据界面

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getPinyinName() {
        return pinyinName;
    }

    public void setPinyinName(String pinyinName) {
        this.pinyinName = pinyinName;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public List<String> getDiseaseNames() {
        return diseaseNames;
    }

    public void setDiseaseNames(List<String> diseaseNames) {
        this.diseaseNames = diseaseNames;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
