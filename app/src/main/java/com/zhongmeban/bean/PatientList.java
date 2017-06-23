package com.zhongmeban.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 获取关注人列表 bean
 * Created by Chengbin He on 2016/7/25.
 */
public class PatientList implements Serializable{

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        private Boolean finish;
        private String serverTime;
        private List<Source> source;

        public Boolean getFinish() {
            return finish;
        }

        public void setFinish(Boolean finish) {
            this.finish = finish;
        }

        public String getServerTime() {
            return serverTime;
        }

        public void setServerTime(String serverTime) {
            this.serverTime = serverTime;
        }

        public List<Source> getSource() {
            return source;
        }

        public void setSource(List<Source> source) {
            this.source = source;
        }
    }

    public class Source{
        private Boolean isActive;
        private String diseaseName;
        private String patientId;
        private String avatar;//头像
        private long comfirmTime;//	确诊日期
        private long birthday;//出生日期
        private int relation;//关注人与用户关系
        private long reviewTime;//复查时间
        private int gender;//性别
        private long diseaseId;
        private int status;
        private int medicineNum;//正在服用药品数量

        public long getReviewTime() {
            return reviewTime;
        }

        public void setReviewTime(long reviewTime) {
            this.reviewTime = reviewTime;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public long getBirthday() {
            return birthday;
        }

        public void setBirthday(long birthday) {
            this.birthday = birthday;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public long getDiseaseId() {
            return diseaseId;
        }

        public void setDiseaseId(long diseaseId) {
            this.diseaseId = diseaseId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public Boolean getActive() {
            return isActive;
        }

        public void setActive(Boolean active) {
            isActive = active;
        }

        public String getDiseaseName() {
            return diseaseName;
        }

        public void setDiseaseName(String diseaseName) {
            this.diseaseName = diseaseName;
        }

        public String getPatientId() {
            return patientId;
        }

        public void setPatientId(String patientId) {
            this.patientId = patientId;
        }

        public long getComfirmTime() {
            return comfirmTime;
        }

        public void setComfirmTime(long comfirmTime) {
            this.comfirmTime = comfirmTime;
        }

        public int getRelation() {
            return relation;
        }

        public void setRelation(int relation) {
            this.relation = relation;
        }

        public int getMedicineNum() {
            return medicineNum;
        }

        public void setMedicineNum(int medicineNum) {
            this.medicineNum = medicineNum;
        }
    }
}
