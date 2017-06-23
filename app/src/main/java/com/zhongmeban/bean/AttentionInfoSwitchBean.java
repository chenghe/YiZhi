package com.zhongmeban.bean;

/**
 * 关注详细记录筛选
 * Created by Chengbin He on 2016/9/13.
 */
public class AttentionInfoSwitchBean {

    private int id;
    private String name;

    public AttentionInfoSwitchBean(int id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public String toString() {
        return name;
    }
}
