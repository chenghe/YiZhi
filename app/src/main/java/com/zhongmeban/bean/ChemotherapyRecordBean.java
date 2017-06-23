package com.zhongmeban.bean;

import de.greenrobot.dao.attention.Chemotherapy;
import java.io.Serializable;
import java.util.List;

/**
 * 化疗，化疗疗程 Bean
 * Created by Chengbin He on 2016/8/25.
 */
public class ChemotherapyRecordBean implements Serializable{

    private boolean finish;
    private String serverTime;
    private List<Source> source;


    public boolean isFinish() {
        return finish;
    }


    public void setFinish(boolean finish) {
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


    public static class Source implements Serializable{

        private Record record;
        private List<Medicines> medicines;


        public Record getRecord() {
            return record;
        }


        public void setRecord(Record record) {
            this.record = record;
        }


        public List<Medicines> getMedicines() {
            return medicines;
        }


        public void setMedicines(List<Medicines> medicines) {
            this.medicines = medicines;
        }
    }


    public static class Record implements Serializable {

        //"doctorName": "样",
        //    "chemotherapyAim": 1,
        //    "times": 4,
        //    "notes": "",
        //    "patientId": 1,
        //    "notesType": 1,
        //    "startTime": 1483509780905,
        //    "id": 5,
        //    "isActive": true,
        //    "status": 1

        private String doctorName;
        private int chemotherapyAim;
        private int times;//第几次化疗
        private String notes;//备注信息
        private String patientId;
        private int notesType;
        private long startTime;
        private long id;
        private int status;
        private int weeksCount;
        private boolean isActive;

        private String medicinesName;//所有药品的名字合集


        public Record(Chemotherapy chemotherapy) {
            this.doctorName = chemotherapy.getDoctorName();
            this.chemotherapyAim = chemotherapy.getChemotherapyAim();
            this.times = chemotherapy.getTimes();
            this.notes = chemotherapy.getNotes();
            this.patientId = chemotherapy.getPatientId();
            this.notesType = chemotherapy.getNotesType();
            this.startTime = chemotherapy.getStartTime();
            this.id = chemotherapy.getId();
            this.status = chemotherapy.getStatus();
            this.weeksCount = chemotherapy.getWeeksCount();
        }


        public String getMedicinesName() {
            return medicinesName;
        }


        public void setMedicinesName(String medicinesName) {
            this.medicinesName = medicinesName;
        }


        public String getDoctorName() {
            return doctorName;
        }


        public void setDoctorName(String doctorName) {
            this.doctorName = doctorName;
        }


        public int getNotesType() {
            return notesType;
        }


        public void setNotesType(int notesType) {
            this.notesType = notesType;
        }


        public int getWeeksCount() {
            return weeksCount;
        }


        public void setWeeksCount(int weeksCount) {
            this.weeksCount = weeksCount;
        }


        public String getNotes() {
            return notes;
        }


        public void setNotes(String notes) {
            this.notes = notes;
        }


        public int getTimes() {
            return times;
        }


        public void setTimes(int times) {
            this.times = times;
        }


        public int getChemotherapyAim() {
            return chemotherapyAim;
        }


        public void setChemotherapyAim(int chemotherapyAim) {
            this.chemotherapyAim = chemotherapyAim;
        }


        public String getPatientId() {
            return patientId;
        }


        public void setPatientId(String patientId) {
            this.patientId = patientId;
        }


        public int getStatus() {
            return status;
        }


        public void setStatus(int status) {
            this.status = status;
        }


        public long getId() {
            return id;
        }


        public void setId(long id) {
            this.id = id;
        }


        public long getStartTime() {
            return startTime;
        }


        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }


        public boolean isActive() {
            return isActive;
        }


        public void setActive(boolean active) {
            isActive = active;
        }
    }


    public static class Medicines implements Serializable{
        private String chemicalName;//丙硫氧嘧啶肠溶胶囊
        private String medicineName;//敖康欣
        private String showName;//敖康欣 (丙硫氧嘧啶肠溶胶囊)
        private int medicineId;


        public Medicines(String chemicalName, String medicineName, String showName, int medicineId) {
            this.chemicalName = chemicalName;
            this.medicineName = medicineName;
            this.showName = showName;
            this.medicineId = medicineId;
        }


        @Override public boolean equals(Object o) {
            if (o instanceof Medicines) {
                Medicines item = (Medicines) o;
                return this.medicineId == item.getMedicineId();
            }
            return false;
        }


        public String getShowName() {
            return showName;
        }


        public void setShowName(String showName) {
            this.showName = showName;
        }


        public String getChemicalName() {
            return chemicalName;
        }


        public void setChemicalName(String chemicalName) {
            this.chemicalName = chemicalName;
        }


        public String getMedicineName() {
            return medicineName;
        }


        public void setMedicineName(String medicineName) {
            this.medicineName = medicineName;
        }


        public int getMedicineId() {
            return medicineId;
        }


        public void setMedicineId(int medicineId) {
            this.medicineId = medicineId;
        }
    }
}
