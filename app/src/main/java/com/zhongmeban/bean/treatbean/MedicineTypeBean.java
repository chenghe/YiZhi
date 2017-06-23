package com.zhongmeban.bean.treatbean;

import java.io.Serializable;

/**
 * 功能描述：
 * 作者：ysf on 2016/12/2 11:06
 */
public class MedicineTypeBean implements Serializable {

    private int id;
    private String name;


    public MedicineTypeBean(int id, String name) {
        this.id = id;
        this.name = name;
    }


    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    @Override public String toString() {
        return "MedicineTypeBean{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }
}
