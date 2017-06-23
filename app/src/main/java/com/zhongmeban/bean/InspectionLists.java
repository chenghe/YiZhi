package com.zhongmeban.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:检查项目的列表
 * user: Chong Chen
 * time：2016/1/18 11:05
 * 邮箱：cchen@ideabinder.com
 */
public class InspectionLists extends BaseBean implements Serializable {

    private InspectionData data;

    public InspectionData getData() {
        return data;
    }

    public void setData(InspectionData data) {
        this.data = data;
    }

    public class InspectionData implements Serializable {
        private long serverTime;
        private List<InspectionScource> source;
        private boolean finish;

        public boolean isFinish() {
            return finish;
        }

        public void setFinish(boolean finish) {
            this.finish = finish;
        }

        public long getServerTime() {
            return serverTime;
        }

        public void setServerTime(long serverTime) {
            this.serverTime = serverTime;
        }

        public List<InspectionScource> getSource() {
            return source;
        }

        public void setSource(List<InspectionScource> source) {
            this.source = source;
        }
    }

    public class InspectionScource implements Serializable {
        private boolean insurance;
        private String name;
        private String pinyinFullName;
        private String fullName;
        private String pinyinName;
        private int id;
        private boolean isActive;
        private int status;
        private Long updateTime;


        @Override public String toString() {
            return "InspectionScource{" +
                "insurance=" + insurance +
                ", name='" + name + '\'' +
                ", pinyinFullName='" + pinyinFullName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", pinyinName='" + pinyinName + '\'' +
                ", id=" + id +
                ", isActive=" + isActive +
                ", status=" + status +
                ", updateTime=" + updateTime +
                '}';
        }


        public boolean isInsurance() {
            return insurance;
        }


        public String getPinyinFullName() {
            return pinyinFullName;
        }


        public String getFullName() {
            return fullName;
        }


        public boolean isActive() {
            return isActive;
        }


        public Long getUpdateTime() {
            return updateTime;
        }


        public String getPinyinName() {
            return pinyinName;
        }

        public void setPinyinName(String pinyinName) {
            this.pinyinName = pinyinName;
        }


        public boolean getIsInsurance() {
            return insurance;
        }

        public void setInsurance(boolean insurance) {
            this.insurance = insurance;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(boolean isActive) {
            this.isActive = isActive;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

    }
}
