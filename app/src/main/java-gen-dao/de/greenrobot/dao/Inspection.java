package de.greenrobot.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "INSPECTION".
 */
public class Inspection extends BaseDao implements java.io.Serializable {

    private long id;
    private Long updateTime;
//    private String name;
//    private String pinyinName;
    private String pinyinFullName;
    private String fullName;
    private Integer status;
    private Boolean isActive;
    private Boolean insurance;

    public Inspection() {
    }

    public Inspection(long id) {
        this.id = id;
    }

    public Inspection(long id, Long updateTime, String name, String pinyinName, String pinyinFullName, String fullName, Integer status, Boolean isActive, Boolean insurance) {
        this.id = id;
        this.updateTime = updateTime;
        this.name = name;
        this.pinyinName = pinyinName;
        this.pinyinFullName = pinyinFullName;
        this.fullName = fullName;
        this.status = status;
        this.isActive = isActive;
        this.insurance = insurance;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyinName() {
        return pinyinName;
    }

    public void setPinyinName(String pinyinName) {
        this.pinyinName = pinyinName;
    }

    public String getPinyinFullName() {
        return pinyinFullName;
    }

    public void setPinyinFullName(String pinyinFullName) {
        this.pinyinFullName = pinyinFullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getInsurance() {
        return insurance;
    }

    public void setInsurance(Boolean insurance) {
        this.insurance = insurance;
    }

}
