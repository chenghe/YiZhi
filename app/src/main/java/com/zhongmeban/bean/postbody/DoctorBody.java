package com.zhongmeban.bean.postbody;

/**
 * 医生列表Post请求Body
 * Created by Chengbin He on 2016/4/26.
 */
public class DoctorBody extends PageBody {
    int skip; // 从第几条开始计算
    String orderBy; // 排序规则,医生推荐度
    String name; //	医生姓名
    String diseaseId; // 擅长癌症
    String cityId; //	所在城市
    String districtId; // 所在城区
    String hospitalId; // 所在医院Id
    String hospitalLevel; // 所在医院等级
    String doctorTitle; // 医生等级
    String isOperation; // 是否可手术 1可手术，2不可手术，0全部


    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(String diseaseId) {
        this.diseaseId = diseaseId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getHospitalLevel() {
        return hospitalLevel;
    }

    public void setHospitalLevel(String hospitalLevel) {
        this.hospitalLevel = hospitalLevel;
    }

    public String getDoctorTitle() {
        return doctorTitle;
    }

    public void setDoctorTitle(String doctorTitle) {
        this.doctorTitle = doctorTitle;
    }

    public String getIsOperation() {
        return isOperation;
    }

    public void setIsOperation(String isOperation) {
        this.isOperation = isOperation;
    }
}
