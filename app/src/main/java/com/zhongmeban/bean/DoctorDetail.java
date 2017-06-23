package com.zhongmeban.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:医生详情中的简介
 * user: Chong Chen
 * time：2016/1/14 17:41
 * 邮箱：cchen@ideabinder.com
 */
public class DoctorDetail extends BaseBean {

    private DoctorDetailData data;

    public DoctorDetailData getData() {
        return data;
    }

    public void setData(DoctorDetailData data) {
        this.data = data;
    }

    public class DoctorDetailData implements Serializable {
        private String doctorId;
        private String doctorName;
        private String notes;
        private boolean isFavorite;

        private List<Doctor.Disease> diseases;
        private List<String> operations;


        public String getNotes() {
            return notes;
        }


        public boolean isFavorite() {
            return isFavorite;
        }


        public String getDoctorId() {
            return doctorId;
        }

        public void setDoctorId(String doctorId) {
            this.doctorId = doctorId;
        }

        public String getDoctorName() {
            return doctorName;
        }

        public void setDoctorName(String doctorName) {
            this.doctorName = doctorName;
        }

        public String getDescription() {
            return notes;
        }

        public void setDescription(String description) {
            this.notes = description;
        }

        public List<Doctor.Disease> getDiseases() {
            return diseases;
        }

        public void setDiseases(List<Doctor.Disease> diseases) {
            this.diseases = diseases;
        }

        public List<String> getOperations() {
            return operations;
        }

        public void setOperations(List<String> operations) {
            this.operations = operations;
        }
    }
}
