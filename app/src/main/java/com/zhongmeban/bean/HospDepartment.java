package com.zhongmeban.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:医院详情之科室床位
 * user: Chong Chen
 * time：2016/1/21 12:36
 * 邮箱：cchen@ideabinder.com
 */
public class HospDepartment extends BaseBean {
    private List<HospDepartData> data;

    public List<HospDepartData> getData() {
        return data;
    }

    public void setData(List<HospDepartData> data) {
        this.data = data;
    }

    public class HospDepartData implements Serializable {
        private String departmentName;
        private int benNum;
        private String departmentId;
        private long updateTime;

        public String getDepartmentName() {
            return departmentName;
        }

        public void setDepartmentName(String departmentName) {
            this.departmentName = departmentName;
        }

        public int getBenNum() {
            return benNum;
        }

        public void setBenNum(int benNum) {
            this.benNum = benNum;
        }

        public String getDepartmentId() {
            return departmentId;
        }

        public void setDepartmentId(String departmentId) {
            this.departmentId = departmentId;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }
    }
}
