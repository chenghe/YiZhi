package com.zhongmeban.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Chengbin He on 2016/4/26.
 */
public class Doctor implements Serializable {
    private int doctorId;
    private String doctorName;
    private String avatar;//缺失数据
    private int gender;
    private String description;
    private String hospitalName;
    private String pinyinName;
    private int profession;
    private int title;
    private List<String> diseaseNames;//返回的疾病是对象
    private boolean operation;
    private String evaluate;//评分
    public int viewType = 1;//0为上拉刷新界面，1为数据界面

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getPinyinName() {
        return pinyinName;
    }

    public void setPinyinName(String pinyinName) {
        this.pinyinName = pinyinName;
    }


    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public int getProfession() {
        return profession;
    }

    public void setProfession(int profession) {
        this.profession = profession;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public List<String> getDiseaseNames() {
        return diseaseNames;
    }

    public void setDiseaseNames(List<String> diseaseNames) {
        this.diseaseNames = diseaseNames;
    }

    public boolean isOperation() {
        return operation;
    }

    public void setOperation(boolean operation) {
        this.operation = operation;
    }

    public String getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }

    public class Disease implements Serializable {
        private long createTime;
        private String name;
        private long updateTime;
        private int id;
        private int status;

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }


        @Override public String toString() {
            return "Disease{" +
                "createTime=" + createTime +
                ", name='" + name + '\'' +
                ", updateTime=" + updateTime +
                ", id=" + id +
                ", status=" + status +
                '}';
        }
    }


    @Override public String toString() {
        return "Doctor{" +
            "doctorId=" + doctorId +
            ", doctorName='" + doctorName + '\'' +
            ", avatar='" + avatar + '\'' +
            ", gender=" + gender +
            ", description='" + description + '\'' +
            ", hospitalName='" + hospitalName + '\'' +
            ", pinyinName='" + pinyinName + '\'' +
            ", profession=" + profession +
            ", title=" + title +
            ", diseaseNames=" + diseaseNames +
            ", operation=" + operation +
            ", evaluate='" + evaluate + '\'' +
            ", viewType=" + viewType +
            '}';
    }
}
