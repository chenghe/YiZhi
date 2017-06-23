package de.greenrobot.dao;

import java.io.Serializable;

/**
 * 关注用药，用于显示数据
 * Created by Chengbin He on 2016/8/5.
 */
public class AttentionMedicine implements Serializable{

    private Medicine medicine;
    private int checkMedicine;//选中用药 1 为选中 2为未选中
    private int type;//用药类型
    private String beginTime;
    private String endTime;

    public AttentionMedicine(Medicine medicine,int checkMedicine,int type,String beginTime){
        this.medicine = medicine;
        this.checkMedicine = checkMedicine;
        this.type = type;
        this.beginTime = beginTime;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public int getCheckMedicine() {
        return checkMedicine;
    }

    public void setCheckMedicine(int checkMedicine) {
        this.checkMedicine = checkMedicine;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
