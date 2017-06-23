package com.zhongmeban.bean.postbody;

/**
 * Created by Chengbin He on 2016/5/23.
 */
public class HospitalBody extends PageBody{
    private String level;
    private String cityId;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }
}
