package com.zhongmeban.bean;

import java.io.Serializable;

/**
 * Created by Chengbin He on 2016/5/3.
 */
public class MedicineScourceItem implements Serializable {
    private String name;
    private String logo;
    private String id;
    private String pinyinName;
    private int type;
    public int viewType = 1;//0为上拉刷新界面，1为数据界面

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getPinyinName() {
        return pinyinName;
    }

    public void setPinyinName(String pinyinName) {
        this.pinyinName = pinyinName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}