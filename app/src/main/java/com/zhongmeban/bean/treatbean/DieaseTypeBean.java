package com.zhongmeban.bean.treatbean;

import java.io.Serializable;

/**
 * 功能描述：
 * 作者：ysf on 2016/12/6 10:32
 */
public class DieaseTypeBean implements Serializable {

    private int id;
    private String name;


    public DieaseTypeBean(int id, String name) {
        this.id = id;
        this.name = name;
    }


    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }
}
