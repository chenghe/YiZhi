package com.zhongmeban.bean.postbody;

import java.util.List;

/**
 * 创建化疗记录,修改化疗记录
 * Created by Chengbin He on 2016/8/23.
 */
public class CreateOrUpdateChemotherapyBody {

    private Record record;
    private List<Integer> medicineIds;


    public CreateOrUpdateChemotherapyBody() {
        record = new Record();
    }


    public Record getRecord() {
        return record;
    }


    public void setRecord(Record record) {
        this.record = record;
    }


    public List<Integer> getMedicineIds() {
        return medicineIds;
    }


    public void setMedicineIds(List<Integer> medicineIds) {
        this.medicineIds = medicineIds;
    }


    public class Record {
        private String patientId;
        private long id;
        private long startTime;
        private int chemotherapyAim;//	化疗目的
        private int weeksCount;//	化疗周期
        private String notes;
        private int notesType;//治疗备注
        private String doctorName;//


        public String getNotes() {
            return notes;
        }


        public void setNotes(String notes) {
            this.notes = notes;
        }


        public String getPatientId() {
            return patientId;
        }


        public long getId() {
            return id;
        }


        public void setId(long id) {
            this.id = id;
        }


        public void setPatientId(String patientId) {
            this.patientId = patientId;
        }


        public long getStartTime() {
            return startTime;
        }


        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }


        public int getChemotherapyAim() {
            return chemotherapyAim;
        }


        public int getWeeksCount() {
            return weeksCount;
        }


        public void setWeeksCount(int weeksCount) {
            this.weeksCount = weeksCount;
        }


        public int getNotesType() {
            return notesType;
        }


        public void setNotesType(int notesType) {
            this.notesType = notesType;
        }


        public String getDoctorName() {
            return doctorName;
        }


        public void setDoctorName(String doctorName) {
            this.doctorName = doctorName;
        }


        public void setChemotherapyAim(int chemotherapyAim) {
            this.chemotherapyAim = chemotherapyAim;
        }
    }

}
