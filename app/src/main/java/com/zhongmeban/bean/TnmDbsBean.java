package com.zhongmeban.bean;

/**
 * Created by User on 2016/9/19.
 */
public class TnmDbsBean extends BaseBean {

    private String tnmName;
    private String notes;
    private int type;


    public TnmDbsBean(String tnmName, String notes, int type) {
        this.tnmName = tnmName;
        this.notes = notes;
        this.type = type;
    }


    public String getTnmName() {
        return tnmName;
    }


    public String getNotes() {
        return notes;
    }


    public int getType() {
        return type;
    }
}
