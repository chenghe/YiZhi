package com.zhongmeban.bean.postbody;

import java.io.Serializable;

/**
 * 关注指标indexs
 * Created by Chengbin He on 2016/7/29.
 */
public class AttentionMarkerIndexs implements Serializable {

    private int id;//指标项Id
    private int indexId;//真正的指标Id
    private String value;//	指标值
    private long time;//检查时间
    private String name;//指标名称
    private String unit;//指标单位


    public AttentionMarkerIndexs(int id, int indexId, String value, long time, String name, String unit) {
        this.id = id;
        this.indexId = indexId;
        this.value = value;
        this.time = time;
        this.name = name;
        this.unit = unit;
    }


    public AttentionMarkerIndexs( ) {
    }


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


    public String getValue() {
        return value;
    }


    public void setValue(String value) {
        this.value = value;
    }


    public long getTime() {
        return time;
    }


    public void setTime(long time) {
        this.time = time;
    }


    public int getIndexId() {
        return indexId;
    }


    public void setIndexId(int indexId) {
        this.indexId = indexId;
    }


    @Override public boolean equals(Object o) {
        if (o instanceof AttentionMarkerIndexs) {
            AttentionMarkerIndexs item = (AttentionMarkerIndexs) o;
            return this.indexId == item.getIndexId();
        }
        return false;
    }
}
