package com.zhongmeban.bean.postbody;

/**
 * Created by Chengbin He on 2016/8/10.
 */
public class SimpleDoctorPageSourceItems {
    public int viewType = 1;//0为上拉刷新界面，1为数据界面
    private String doctorName;
    private int doctorId;
    private String hospitalName;
    private int title;

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }
}
