package com.zhongmeban.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:医生列表实体
 * user: Chong Chen
 * time：2016/1/12 20:44
 * 邮箱：cchen@ideabinder.com
 */
public class DoctorList extends BaseBean {

    private DoctorListData data;

    public DoctorListData getData() {
        return data;
    }

    public void setData(DoctorListData data) {
        this.data = data;
    }

    public class DoctorListData implements Serializable {

        private int startCount;
        private int totalPage;
        private int endCount;
        private int pageSize;
        private List<Doctor> sourceItems;
        private int totalCount;
        private int indexPage;

        public List<Doctor> getSourceItems() {
            return sourceItems;
        }

        public void setSourceItems(List<Doctor> sourceItems) {
            this.sourceItems = sourceItems;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
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


        @Override public String toString() {
            return "DoctorListData{" +
                "startCount=" + startCount +
                ", totalPage=" + totalPage +
                ", endCount=" + endCount +
                ", pageSize=" + pageSize +
                ", sourceItems=" + sourceItems +
                ", totalCount=" + totalCount +
                ", indexPage=" + indexPage +
                '}';
        }
    }


    @Override public String toString() {
        return "DoctorList{" +
            "data=" + data +
            '}';
    }
}
