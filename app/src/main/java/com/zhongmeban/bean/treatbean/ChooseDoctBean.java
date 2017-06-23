package com.zhongmeban.bean.treatbean;

import com.zhongmeban.bean.DropDownBean;

import java.io.Serializable;


/**
 * 医生搜索 筛选条件
 * Created by Chengbin He on 2016/12/17.
 */

public class ChooseDoctBean implements Serializable{

    private CityListBean cityChoose;
    private DropDownBean hospLevelChoose;
    private DropDownBean doctLevelChoose;


    public ChooseDoctBean(CityListBean cityChoose, DropDownBean hospLevelChoose,DropDownBean doctLevelChoose) {
        this.cityChoose = cityChoose;
        this.hospLevelChoose = hospLevelChoose;
        this.doctLevelChoose = doctLevelChoose;
    }

    public DropDownBean getDoctLevelChoose() {
        return doctLevelChoose;
    }

    public void setDoctLevelChoose(DropDownBean doctLevelChoose) {
        this.doctLevelChoose = doctLevelChoose;
    }

    public CityListBean getCityChoose() {
        return cityChoose;
    }

    public void setCityChoose(CityListBean cityChoose) {
        this.cityChoose = cityChoose;
    }

    public DropDownBean getHospLevelChoose() {
        return hospLevelChoose;
    }

    public void setHospLevelChoose(DropDownBean hospLevelChoose) {
        this.hospLevelChoose = hospLevelChoose;
    }
}
