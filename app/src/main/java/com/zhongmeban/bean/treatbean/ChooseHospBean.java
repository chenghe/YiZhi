package com.zhongmeban.bean.treatbean;

import com.zhongmeban.bean.DropDownBean;

import java.io.Serializable;


/**
 * 医院搜索 筛选条件
 * Created by Chengbin He on 2016/12/17.
 */

public class ChooseHospBean implements Serializable{

    private CityListBean cityChoose;
    private DropDownBean hospLevelChoose;


    public ChooseHospBean(CityListBean cityChoose, DropDownBean hospLevelChoose) {
        this.cityChoose = cityChoose;
        this.hospLevelChoose = hospLevelChoose;
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
