package com.zhongmeban.bean.treatbean;

import java.io.Serializable;

/**
 * Created by Chengbin He on 2016/12/6.
 */

public class CityListBean implements Serializable{

    private String cityCode;
    private String name;

    public CityListBean( String cityCode,String name) {
        this.name = name;
        this.cityCode = cityCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }
}
