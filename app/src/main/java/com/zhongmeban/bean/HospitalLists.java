package com.zhongmeban.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:医院列表
 * user: Chong Chen
 * time：2016/1/19 14:53
 * 邮箱：cchen@ideabinder.com
 */
public class HospitalLists extends BaseBean implements Serializable {

    private HospitalListData data;
    public int viewType;

    public HospitalListData getData() {
        return data;
    }

    public void setData(HospitalListData data) {
        this.data = data;
    }

    public class HospitalListData implements Serializable {
        private List<HospitalScource> sourceItems;
        private int totalCount;
        private int totalPage;
        private int indexPage;
        private int startCount;
        private int endCount;
        private int pageSize;

        public List<HospitalScource> getSourceItems() {
            return sourceItems;
        }

        public void setSourceItems(List<HospitalScource> sourceItems) {
            this.sourceItems = sourceItems;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getIndexPage() {
            return indexPage;
        }

        public void setIndexPage(int indexPage) {
            this.indexPage = indexPage;
        }

        public int getStartCount() {
            return startCount;
        }

        public void setStartCount(int startCount) {
            this.startCount = startCount;
        }

        public int getEndCount() {
            return endCount;
        }

        public void setEndCount(int endCount) {
            this.endCount = endCount;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }
    }

//    public class HospitalScource implements Serializable {
//        private int hospitalId;
//        private int level;
//        private int waitDay;
//        private String hospitalName;
//        private String pinyinName;
//        private String address;
//        private String picture;
//
//
//        public int getHospitalId() {
//            return hospitalId;
//        }
//
//        public void setHospitalId(int hospitalId) {
//            this.hospitalId = hospitalId;
//        }
//
//        public int getLevel() {
//            return level;
//        }
//
//        public void setLevel(int level) {
//            this.level = level;
//        }
//
//        public int getWaitDay() {
//            return waitDay;
//        }
//
//        public void setWaitDay(int waitDay) {
//            this.waitDay = waitDay;
//        }
//
//        public String getHospitalName() {
//            return hospitalName;
//        }
//
//        public void setHospitalName(String hospitalName) {
//            this.hospitalName = hospitalName;
//        }
//
//        public String getAddress() {
//            return address;
//        }
//
//        public void setAddress(String address) {
//            this.address = address;
//        }
//
//        public String getPinyinName() {
//            return pinyinName;
//        }
//
//        public void setPinyinName(String pinyinName) {
//            this.pinyinName = pinyinName;
//        }
//
//        public String getPicture() {
//            return picture;
//        }
//
//        public void setPicture(String picture) {
//            this.picture = picture;
//        }
//    }
}

