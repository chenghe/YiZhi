package com.zhongmeban.bean.treatbean;

import java.io.Serializable;

/**
 * 功能描述：
 * 作者：ysf on 2016/12/6 10:33
 */
public class TreatMethodCommonBean implements Serializable {

    private int id;
    private String name;
    private String chemicalName;
    private String showName;


    public int getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public String getChemicalName() {
        return chemicalName;
    }


    public String getShowName() {
        return showName;
    }
}
