package com.zhongmeban.bean;

import java.io.Serializable;

/**
 * Description:医院详情的简介
 * user: Chong Chen
 * time：2016/1/21 11:17
 * 邮箱：cchen@ideabinder.com
 */
public class HospitalDetail extends BaseBean {
    private HospData data;

    public HospData getData() {
        return data;
    }

    public void setData(HospData data) {
        this.data = data;
    }

    public class HospData implements Serializable {
        private String contectInfo;
        private String notes;
        private String id;
        private String level;
        private String doctorCount;

        public String getDoctorCount() {
            return doctorCount;
        }

        public void setDoctorCount(String doctorCount) {
            this.doctorCount = doctorCount;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getContectInfo() {
            return contectInfo;
        }

        public void setContectInfo(String contectInfo) {
            this.contectInfo = contectInfo;
        }

        public String getDescription() {
            return notes;
        }

        public void setDescription(String description) {
            notes = description;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

}
