package com.zhongmeban.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:指标列表
 * user: Chong Chen
 * time：2016/1/21 19:15
 * 邮箱：cchen@ideabinder.com
 */
public class IndiacatorList extends BaseBean {
    private IndiacatorListData data;

    public IndiacatorListData getData() {
        return data;
    }

    public void setData(IndiacatorListData data) {
        this.data = data;
    }

    public class IndiacatorListData implements Serializable {
        private List<IndiacatorSource> source;
        private long serverTime;
        private boolean finish;

        public boolean isFinish() {
            return finish;
        }

        public void setFinish(boolean finish) {
            this.finish = finish;
        }

        public List<IndiacatorSource> getSource() {
            return source;
        }

        public void setSource(List<IndiacatorSource> source) {
            this.source = source;
        }

        public long getServerTime() {
            return serverTime;
        }

        public void setServerTime(long serverTime) {
            this.serverTime = serverTime;
        }
    }


    public class IndiacatorSource implements Serializable {
        private String name;
        private int id;
        private int status;
        private Boolean isActive;
        private Boolean isLandmark;//是否为标志物
        private String pinyinName;
        private String pinyinFullName;
        private String fullName;
        private String unit;//单位名称
        //long id, Long updateTime, Integer status, String name, String pinyinName, String pinyinFullName, String fullName, Boolean isActive

        private  Long updateTime;


        public Boolean getLandmark() {
            return isLandmark;
        }

        public void setLandmark(Boolean landmark) {
            isLandmark = landmark;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public Boolean getActive() {
            return isActive;
        }


        public String getPinyinFullName() {
            return pinyinFullName;
        }


        public String getFullName() {
            return fullName;
        }


        public Long getUpdateTime() {
            return updateTime;
        }


        public void setActive(Boolean active) {
            isActive = active;
        }


        public void setPinyinFullName(String pinyinFullName) {
            this.pinyinFullName = pinyinFullName;
        }


        public void setFullName(String fullName) {
            this.fullName = fullName;
        }


        public void setUpdateTime(Long updateTime) {
            this.updateTime = updateTime;
        }


        public String getPinyinName() {
            return pinyinName;
        }

        public void setPinyinName(String pinyinName) {
            this.pinyinName = pinyinName;
        }

        public Boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(Boolean isActive) {
            this.isActive = isActive;
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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }


}
