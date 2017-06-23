package com.zhongmeban.bean.treatbean;

import java.io.Serializable;

/**
 * 1.0.1
 * Created by Chengbin He on 2016/4/25.
 */
public class TreatHospitalScource implements Serializable {
    private int id;
    private int level;
    private String name;
    private String address;
    private String picture;
    public int viewType = 1;//0为上拉刷新界面，1为数据界面


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
