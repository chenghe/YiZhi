package com.zhongmeban.bean.attentionbean;

import de.greenrobot.dao.attention.MedicineRecord;
import java.io.Serializable;
import java.util.List;

/**
 * 功能描述：
 * 作者：ysf on 2017/1/6 17:22
 */
public class AssistMedicineListBean implements Serializable {

    /**
     * serverTime : 1483694035215
     * finish : true
     * source : [{"medicineRecordDbs":[{"patientId":1,"createTime":1483688909236,"medicineId":528,"startTime":1483632000000,"updateTime":1483688909236,"endTime":1483718400000,"id":7,"isActive":true,"medicineItemId":6,"medicineName":"阿拉明","status":2}],"patientId":1,"createTime":1483688909232,"startTime":1483632000000,"updateTime":1483688909232,"endTime":1483718400000,"id":6,"isActive":true},{"medicineRecordDbs":[{"patientId":1,"createTime":1483691638410,"medicineId":364,"startTime":1483632000000,"updateTime":1483691638410,"endTime":1483632000000,"id":8,"isActive":true,"medicineItemId":7,"medicineName":"敖康欣","status":2}],"patientId":1,"createTime":1483691638407,"startTime":1483632000000,"updateTime":1483691638407,"endTime":1483632000000,"id":7,"isActive":true},{"medicineRecordDbs":[{"patientId":1,"createTime":1483692718469,"medicineId":1134,"startTime":1483632000000,"updateTime":1483692718469,"id":9,"isActive":true,"medicineItemId":8,"medicineName":"氨甲苯酸片","status":1}],"patientId":1,"createTime":1483692718466,"startTime":1483632000000,"updateTime":1483692718466,"id":8,"isActive":true},{"medicineRecordDbs":[{"patientId":1,"createTime":1483692718481,"medicineId":528,"startTime":1483632000000,"updateTime":1483692718481,"id":10,"isActive":true,"medicineItemId":9,"medicineName":"阿拉明","status":1}],"patientId":1,"createTime":1483692718478,"startTime":1483632000000,"updateTime":1483692718478,"id":9,"isActive":true},{"medicineRecordDbs":[{"patientId":1,"createTime":1483692764389,"medicineId":701,"startTime":1483632000000,"updateTime":1483692764389,"id":11,"isActive":true,"medicineItemId":10,"medicineName":"Adriamycin","status":1}],"patientId":1,"createTime":1483692764386,"startTime":1483632000000,"updateTime":1483692764386,"id":10,"isActive":true},{"medicineRecordDbs":[{"patientId":1,"createTime":1483692764401,"medicineId":1134,"startTime":1483632000000,"updateTime":1483692764401,"id":12,"isActive":true,"medicineItemId":11,"medicineName":"氨甲苯酸片","status":1}],"patientId":1,"createTime":1483692764399,"startTime":1483632000000,"updateTime":1483692764399,"id":11,"isActive":true},{"medicineRecordDbs":[{"patientId":1,"createTime":1483694034945,"medicineId":364,"startTime":1483632000000,"updateTime":1483694034945,"id":13,"isActive":true,"medicineItemId":12,"medicineName":"敖康欣","status":1}],"patientId":1,"createTime":1483694034941,"startTime":1483632000000,"updateTime":1483694034941,"id":12,"isActive":true},{"medicineRecordDbs":[{"patientId":1,"createTime":1483694034960,"medicineId":619,"startTime":1483632000000,"updateTime":1483694034960,"id":14,"isActive":true,"medicineItemId":13,"medicineName":"安道生","status":1}],"patientId":1,"createTime":1483694034956,"startTime":1483632000000,"updateTime":1483694034956,"id":13,"isActive":true}]
     */

    private Long serverTime;
    private boolean finish;
    private List<MedicineRecordBean> source;


    public Long getServerTime() { return serverTime;}


    public void setServerTime(Long serverTime) { this.serverTime = serverTime;}


    public boolean isFinish() { return finish;}


    public void setFinish(boolean finish) { this.finish = finish;}


    public List<MedicineRecordBean> getSource() { return source;}


    public void setSource(List<MedicineRecordBean> source) { this.source = source;}


    public static class MedicineRecordBean implements Serializable {
        /**
         * medicineRecordDbs : [{"patientId":1,"createTime":1483688909236,"medicineId":528,"startTime":1483632000000,"updateTime":1483688909236,"endTime":1483718400000,"id":7,"isActive":true,"medicineItemId":6,"medicineName":"阿拉明","status":2}]
         * patientId : 1
         * createTime : 1483688909232
         * startTime : 1483632000000
         * updateTime : 1483688909232
         * endTime : 1483718400000
         * id : 6
         * isActive : true
         */

        private int patientId;
        private Long createTime;
        private Long startTime;
        private Long updateTime;
        private Long endTime;
        private int id;
        private boolean isActive;
        private List<MedicineRecordDbsBean> medicineRecordDbs;


        public int getPatientId() { return patientId;}


        public void setPatientId(int patientId) { this.patientId = patientId;}


        public Long getCreateTime() { return createTime;}


        public void setCreateTime(Long createTime) { this.createTime = createTime;}


        public Long getStartTime() { return startTime;}


        public void setStartTime(Long startTime) { this.startTime = startTime;}


        public Long getUpdateTime() { return updateTime;}


        public void setUpdateTime(Long updateTime) { this.updateTime = updateTime;}


        public Long getEndTime() { return endTime;}


        public void setEndTime(Long endTime) { this.endTime = endTime;}


        public int getId() { return id;}


        public void setId(int id) { this.id = id;}


        public boolean isIsActive() { return isActive;}


        public void setIsActive(boolean isActive) { this.isActive = isActive;}


        public List<MedicineRecordDbsBean> getMedicineRecordDbs() { return medicineRecordDbs;}


        @Override public String toString() {
            return "MedicineRecordBean{" +
                "patientId=" + patientId +
                ", createTime=" + createTime +
                ", startTime=" + startTime +
                ", updateTime=" + updateTime +
                ", endTime=" + endTime +
                ", id=" + id +
                ", isActive=" + isActive +
                ", medicineRecordDbs=" + medicineRecordDbs +
                '}';
        }


        public void setMedicineRecordDbs(List<MedicineRecordDbsBean> medicineRecordDbs) {
            this.medicineRecordDbs = medicineRecordDbs;
        }


        public static class MedicineRecordDbsBean implements Serializable {
            /**
             * patientId : 1
             * createTime : 1483688909236
             * medicineId : 528
             * startTime : 1483632000000
             * updateTime : 1483688909236
             * endTime : 1483718400000
             * id : 7
             * isActive : true
             * medicineItemId : 6
             * medicineName : 阿拉明
             * status : 2
             */

            private int patientId;
            private Long createTime;
            private int medicineId;
            private Long startTime;
            private Long updateTime;
            private Long endTime;
            private int id;
            private boolean isActive;
            private int medicineItemId;
            private String medicineName;
            private String chemicalName;
            private int status;


            public MedicineRecordDbsBean() {
            }


            public MedicineRecordDbsBean(MedicineRecord dbData) {
                this.patientId = Integer.valueOf(dbData.getPatientId());
                this.chemicalName = dbData.getChemicalName();
                this.startTime = dbData.getStartTime();
                this.endTime = dbData.getEndTime();
                this.medicineId = dbData.getMedicineId().intValue();
                this.id = (int) dbData.getId();
                this.medicineName = dbData.getMedicineName();
            }


            @Override public String toString() {
                return "MedicineRecordDbsBean{" +
                    "patientId=" + patientId +
                    ", createTime=" + createTime +
                    ", medicineId=" + medicineId +
                    ", startTime=" + startTime +
                    ", updateTime=" + updateTime +
                    ", endTime=" + endTime +
                    ", id=" + id +
                    ", isActive=" + isActive +
                    ", medicineItemId=" + medicineItemId +
                    ", medicineName='" + medicineName + '\'' +
                    ", chemicalName='" + chemicalName + '\'' +
                    ", status=" + status +
                    '}';
            }


            public String getChemicalName() {
                return chemicalName;
            }


            public void setChemicalName(String chemicalName) {
                this.chemicalName = chemicalName;
            }


            public boolean isActive() {
                return isActive;
            }


            public void setActive(boolean active) {
                isActive = active;
            }


            public int getPatientId() { return patientId;}


            public void setPatientId(int patientId) { this.patientId = patientId;}


            public Long getCreateTime() { return createTime;}


            public void setCreateTime(Long createTime) { this.createTime = createTime;}


            public int getMedicineId() { return medicineId;}


            public void setMedicineId(int medicineId) { this.medicineId = medicineId;}


            public Long getStartTime() { return startTime;}


            public void setStartTime(Long startTime) { this.startTime = startTime;}


            public Long getUpdateTime() { return updateTime;}


            public void setUpdateTime(Long updateTime) { this.updateTime = updateTime;}


            public Long getEndTime() { return endTime;}


            public void setEndTime(Long endTime) { this.endTime = endTime;}


            public int getId() { return id;}


            public void setId(int id) { this.id = id;}


            public boolean isIsActive() { return isActive;}


            public void setIsActive(boolean isActive) { this.isActive = isActive;}


            public int getMedicineItemId() { return medicineItemId;}


            public void setMedicineItemId(int medicineItemId) {
                this.medicineItemId = medicineItemId;
            }


            public String getMedicineName() { return medicineName;}


            public void setMedicineName(String medicineName) { this.medicineName = medicineName;}


            public int getStatus() { return status;}


            public void setStatus(int status) { this.status = status;}
        }
    }


    @Override public String toString() {
        return "AssistMedicineListBean{" +
            "serverTime=" + serverTime +
            ", finish=" + finish +
            ", source=" + source +
            '}';
    }
}
