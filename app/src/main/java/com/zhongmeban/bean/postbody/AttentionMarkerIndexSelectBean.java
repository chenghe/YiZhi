package com.zhongmeban.bean.postbody;

import java.io.Serializable;

/**
 * 关注指标indexs
 * Created by Chengbin He on 2016/7/29.
 */
public class AttentionMarkerIndexSelectBean implements Serializable {

    private int id;//真正的指标Id
    private String name;//指标名称
    private String unit;//指标单位


    public String getUnit() {
        return unit;
    }


    public void setUnit(String unit) {
        this.unit = unit;
    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

}
