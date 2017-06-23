package com.zhongmeban.bean;

import java.util.List;

/**
 * Created by Chengbin He on 2016/8/22.
 */
public class RadiotherapyRecordsListBean {

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

    public class Source {
        private long id;
        private long startTime;
        private int resultType;
        private String patientId;
        private String weeksCount;
        private String predictDosage;//放疗剂量
        private String currentCount;//放疗次数
        private String notes;
        private boolean isActive;

        public String getPredictDosage() {
            return predictDosage;
        }

        public void setPredictDosage(String predictDosage) {
            this.predictDosage = predictDosage;
        }

        public String getCurrentCount() {
            return currentCount;
        }

        public void setCurrentCount(String currentCount) {
            this.currentCount = currentCount;
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

        public int getResultType() {
            return resultType;
        }

        public void setResultType(int resultType) {
            this.resultType = resultType;
        }

        public String getPatientId() {
            return patientId;
        }

        public void setPatientId(String patientId) {
            this.patientId = patientId;
        }

        public String getWeeksCount() {
            return weeksCount;
        }

        public void setWeeksCount(String weeksCount) {
            this.weeksCount = weeksCount;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public boolean isActive() {
            return isActive;
        }

        public void setActive(boolean active) {
            isActive = active;
        }
    }
}
