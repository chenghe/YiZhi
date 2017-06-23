package com.zhongmeban.bean.postbody;

import java.util.List;

/**
 * 创建化疗 疗程记录,修改化疗记录
 * Created by Chengbin He on 2016/8/23.
 */
public class CreateOrUpdateChemotherapyCourseBody {

    private Record record;
    private List<Integer> medicineIds;

    public CreateOrUpdateChemotherapyCourseBody() {
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
        private long endTime;
        private long chemotherapyRecordId;
        private String notes;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getPatientId() {
            return patientId;
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

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public long getChemotherapyRecordId() {
            return chemotherapyRecordId;
        }

        public void setChemotherapyRecordId(long chemotherapyRecordId) {
            this.chemotherapyRecordId = chemotherapyRecordId;
        }

        public String getDescription() {
            return notes;
        }

        public void setDescription(String description) {
           notes = description;
        }
    }

}
