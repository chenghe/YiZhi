package com.zhongmeban.bean.postbody;

import java.util.List;

/**
 * Created by Chengbin He on 2016/8/4.
 */
public class CreateMedicineRecordListBody {

        private String patientId;
        private int medicineId;
        private String medicineName;
        private long startTime;
        private long endTime;
        private int status;//用药状态 1使用中 2历史用药
        private int type;//用药类型 1.对症用药 2.靶向用药 4.镇痛用药 8.其他用药
//        private List<Integer>types;

        public String getPatientId() {
            return patientId;
        }

        public void setPatientId(String patientId) {
            this.patientId = patientId;
        }

        public int getMedicineId() {
            return medicineId;
        }

        public void setMedicineId(int medicineId) {
            this.medicineId = medicineId;
        }

        public String getMedicineName() {
            return medicineName;
        }

        public void setMedicineName(String medicineName) {
            this.medicineName = medicineName;
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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

//        public int getTypes() {
//            return types;
//        }

//        public void setTypes(int types) {
//            this.types = types;
//        }


    public int getTypes() {
        return type;
    }

    public void setTypes(int types) {
        type = types;
    }
}


