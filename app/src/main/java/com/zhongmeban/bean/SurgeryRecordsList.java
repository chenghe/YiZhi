package com.zhongmeban.bean;

import java.util.List;

/**
 * 获取手术记录
 * Created by Chengbin He on 2016/8/9.
 */
public class SurgeryRecordsList {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        private String serverTime;
        private boolean finish;
        private List<Source> source;

        public String getServerTime() {
            return serverTime;
        }

        public void setServerTime(String serverTime) {
            this.serverTime = serverTime;
        }

        public boolean isFinish() {
            return finish;
        }

        public void setFinish(boolean finish) {
            this.finish = finish;
        }

        public List<Source> getSource() {
            return source;
        }

        public void setSource(List<Source> source) {
            this.source = source;
        }
    }

    public class Source{
        private SurgeryRecord surgeryRecord;
        private List<SurgeryMethods> surgeryMethods;

        public SurgeryRecord getSurgeryRecord() {
            return surgeryRecord;
        }

        public void setSurgeryRecord(SurgeryRecord surgeryRecord) {
            this.surgeryRecord = surgeryRecord;
        }

        public List<SurgeryMethods> getSurgeryMethods() {
            return surgeryMethods;
        }

        public void setSurgeryMethods(List<SurgeryMethods> surgeryMethods) {
            this.surgeryMethods = surgeryMethods;
        }
    }

    public class SurgeryRecord{
        private String notes;
        private String doctorName;
        private String therapeuticName;
        private String patientId;
        private String hospitalName;
        private int doctorId;
        private int hospitalId;
        private int therapeuticId;
        private int id;
        private long createTime;
        private long updateTime;
        private long time;
        private  long hospitalRecordId;//住院医院ID
        private boolean isActive;

        public long getHospitalRecordId() {
            return hospitalRecordId;
        }

        public void setHospitalRecordId(long hospitalRecordId) {
            this.hospitalRecordId = hospitalRecordId;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public String getDoctorName() {
            return doctorName;
        }

        public void setDoctorName(String doctorName) {
            this.doctorName = doctorName;
        }

        public String getTherapeuticName() {
            return therapeuticName;
        }

        public void setTherapeuticName(String therapeuticName) {
            this.therapeuticName = therapeuticName;
        }

        public String getPatientId() {
            return patientId;
        }

        public void setPatientId(String patientId) {
            this.patientId = patientId;
        }

        public String getHospitalName() {
            return hospitalName;
        }

        public void setHospitalName(String hospitalName) {
            this.hospitalName = hospitalName;
        }

        public int getDoctorId() {
            return doctorId;
        }

        public void setDoctorId(int doctorId) {
            this.doctorId = doctorId;
        }

        public int getHospitalId() {
            return hospitalId;
        }

        public void setHospitalId(int hospitalId) {
            this.hospitalId = hospitalId;
        }

        public int getTherapeuticId() {
            return therapeuticId;
        }

        public void setTherapeuticId(int therapeuticId) {
            this.therapeuticId = therapeuticId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public boolean isActive() {
            return isActive;
        }

        public void setActive(boolean active) {
            isActive = active;
        }
    }

    public class SurgeryMethods{
        private int surgeryMethodId;
        private String surgeryMethodName;

        public int getSurgeryMethodId() {
            return surgeryMethodId;
        }

        public void setSurgeryMethodId(int surgeryMethodId) {
            this.surgeryMethodId = surgeryMethodId;
        }

        public String getSurgeryMethodName() {
            return surgeryMethodName;
        }

        public void setSurgeryMethodName(String surgeryMethodName) {
            this.surgeryMethodName = surgeryMethodName;
        }
    }
}
