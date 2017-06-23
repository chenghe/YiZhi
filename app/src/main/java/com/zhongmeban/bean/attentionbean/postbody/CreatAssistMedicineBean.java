package com.zhongmeban.bean.attentionbean.postbody;

import java.io.Serializable;
import java.util.List;

/**
 * 功能描述：
 * 作者：ysf on 2017/1/6 15:12
 */
public class CreatAssistMedicineBean implements Serializable {

    private List<ItemRecordsBean> itemRecords;


    public List<ItemRecordsBean> getItemRecords() { return itemRecords;}


    public void setItemRecords(List<ItemRecordsBean> itemRecords) { this.itemRecords = itemRecords;}


    public CreatAssistMedicineBean(List<ItemRecordsBean> itemRecords) {
        this.itemRecords = itemRecords;
    }


    public static class ItemRecordsBean {
        /**
         * endTime : 1483718400000
         * medicineRecordDbs : [{"medicineId":528}]
         * patientId : 1
         * startTime : 1483632000000
         */

        private Long endTime;
        private String patientId;
        private Long startTime;
        private List<MedicineRecordDbsBean> medicineRecordDbs;


        public Long getEndTime() { return endTime;}


        public void setEndTime(Long endTime) { this.endTime = endTime;}


        public String getPatientId() { return patientId;}


        public void setPatientId(String patientId) { this.patientId = patientId;}


        public Long getStartTime() { return startTime;}


        public void setStartTime(Long startTime) { this.startTime = startTime;}


        public List<MedicineRecordDbsBean> getMedicineRecordDbs() { return medicineRecordDbs;}


        public void setMedicineRecordDbs(List<MedicineRecordDbsBean> medicineRecordDbs) {
            this.medicineRecordDbs = medicineRecordDbs;
        }


        public ItemRecordsBean(Long endTime, String patientId, Long startTime, List<MedicineRecordDbsBean> medicineRecordDbs) {
            this.endTime = endTime;
            this.patientId = patientId;
            this.startTime = startTime;
            this.medicineRecordDbs = medicineRecordDbs;
        }


        public static class MedicineRecordDbsBean {
            /**
             * medicineId : 528
             */

            private int medicineId;


            public int getMedicineId() { return medicineId;}


            public void setMedicineId(int medicineId) { this.medicineId = medicineId;}


            public MedicineRecordDbsBean(int medicineId) {
                this.medicineId = medicineId;
            }
        }
    }
}
