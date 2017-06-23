package com.zhongmeban.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 获取医生工作时间表 业务Bean
 * Created by Chengbin He on 2016/5/17.
 */
public class DoctorWorkTime extends BaseBean  {
    private DoctorWorkTimeData data;

    public DoctorWorkTimeData getData() {
        return data;
    }

    public void setData(DoctorWorkTimeData data) {
        this.data = data;
    }

    public class DoctorWorkTimeData implements Serializable{
        private List<WorkTimeList> workTimeList;
        private String normalFee;
        private String experFee;
        private String specialFee;

        public String getNormalFee() {
            return normalFee;
        }

        public void setNormalFee(String normalFee) {
            this.normalFee = normalFee;
        }

        public String getExperFee() {
            return experFee;
        }

        public void setExperFee(String experFee) {
            this.experFee = experFee;
        }

        public String getSpecialFee() {
            return specialFee;
        }

        public void setSpecialFee(String specialFee) {
            this.specialFee = specialFee;
        }

        public List<WorkTimeList> getWorkTimeList() {
            return workTimeList;
        }

        public void setWorkTimeList(List<WorkTimeList> workTimeList) {
            this.workTimeList = workTimeList;
        }
    }

    public class WorkTimeList implements Serializable{
        private int week;
        private int hospitalId;
        private int time;
        private int type;// OutpatientTypeNormal = 1  普通门诊,OutpatientTypeExpert = 2 专家门诊 ,OutpatientTypeSpecial = 3 特殊门诊

        public int getWeek() {
            return week;
        }

        public void setWeek(int week) {
            this.week = week;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public int getHospitalId() {
            return hospitalId;
        }

        public void setHospitalId(int hospitalId) {
            this.hospitalId = hospitalId;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
