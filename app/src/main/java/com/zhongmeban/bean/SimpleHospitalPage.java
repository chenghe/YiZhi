package com.zhongmeban.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Chengbin He on 2016/7/26.
 */
public class SimpleHospitalPage implements Serializable{

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{

        private String startCount;
        private String totalPage;
        private String pageSize;
        private String endCount;
        private int totalCount;
        private int indexPage;

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

        private List<SimpleHospitalPageSourceItems> sourceItems;

        public List<SimpleHospitalPageSourceItems> getSourceItems() {
            return sourceItems;
        }

        public void setSourceItems(List<SimpleHospitalPageSourceItems> sourceItems) {
            this.sourceItems = sourceItems;
        }

        public String getStartCount() {
            return startCount;
        }

        public void setStartCount(String startCount) {
            this.startCount = startCount;
        }

        public String getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(String totalPage) {
            this.totalPage = totalPage;
        }

        public String getPageSize() {
            return pageSize;
        }

        public void setPageSize(String pageSize) {
            this.pageSize = pageSize;
        }

        public String getEndCount() {
            return endCount;
        }

        public void setEndCount(String endCount) {
            this.endCount = endCount;
        }

    }

}
