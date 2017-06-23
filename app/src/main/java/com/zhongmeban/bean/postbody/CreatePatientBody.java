package com.zhongmeban.bean.postbody;

/**
 * Created by Chengbin He on 2016/7/25.
 */
public class CreatePatientBody {

    private String userId;
    private String id;//patientId
    private int relation;
    private String relationName;
    private long diseaseId;
    private String diseaseName;
    private String avatar;//图片URL
    private int gender;
    private long birthday;
    private long comfirmTime;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public long getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(long diseaseId) {
        this.diseaseId = diseaseId;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public long getComfirmTime() {
        return comfirmTime;
    }

    public void setComfirmTime(long comfirmTime) {
        this.comfirmTime = comfirmTime;
    }
}
