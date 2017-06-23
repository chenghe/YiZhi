package com.zhongmeban.bean;

import java.util.List;

/**
 * 获取用药记录
 * Created by Chengbin He on 2016/8/4.
 */
public class MedicineRecordBean {
    private Data data;

    public Data getDate() {
        return data;
    }

    public void setDate(Data data) {
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
//        private int types;//用药类型 1.对症用药 2.靶向用药 4.镇痛用药 8.其他用药

        //"medicineRecordDbs": {},
        //    "createTime": 1482918476122,
        //    "id": 3,
        //    "isActive": true,
        //    "patientId": 1,
        //    "startTime": 0,
        //    "updateTime": 1482918476122
        private List<Integer>types;
        private int type;
        private String patientId;
        private long createTime;
        private int medicineId;
        private long startTime;
        private long updateTime;
        private long endTime;
        private int id;
        private boolean isActive;
        private String medicineName;
        private int status;//用药状态 1使用中 2历史用药

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public List<Integer> getTypes() {
            return types;
        }

        public void setTypes(List<Integer> types) {
            this.types = types;
        }

        //        public int getTypes() {
//            return types;
//        }
//
//        public void setTypes(int types) {
//            this.types = types;
//        }

        public String getPatientId() {
            return patientId;
        }

        public void setPatientId(String patientId) {
            this.patientId = patientId;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public int getMedicineId() {
            return medicineId;
        }

        public void setMedicineId(int medicineId) {
            this.medicineId = medicineId;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isActive() {
            return isActive;
        }

        public void setActive(boolean active) {
            isActive = active;
        }

        public String getMedicineName() {
            return medicineName;
        }

        public void setMedicineName(String medicineName) {
            this.medicineName = medicineName;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
