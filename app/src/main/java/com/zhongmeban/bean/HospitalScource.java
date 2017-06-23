package com.zhongmeban.bean;

import java.io.Serializable;

/**
 * Created by Chengbin He on 2016/4/25.
 */
public class HospitalScource implements Serializable {
    private int hospitalId;
    private int level;
    private int waitDay;
    private String hospitalName;
    private String pinyinName;
    private String address;
    private String picture;
    public int viewType = 1;//0为上拉刷新界面，1为数据界面

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getWaitDay() {
        return waitDay;
    }

    public void setWaitDay(int waitDay) {
        this.waitDay = waitDay;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPinyinName() {
        return pinyinName;
    }

    public void setPinyinName(String pinyinName) {
        this.pinyinName = pinyinName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
