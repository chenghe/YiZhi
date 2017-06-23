package com.zhongmeban.bean;

/**
 * Created by Chengbin He on 2016/7/27.
 */
public class SimpleHospitalPageSourceItems {
    private String name;
    private int id;
    public int viewType = 1;//0为上拉刷新界面，1为数据界面

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

