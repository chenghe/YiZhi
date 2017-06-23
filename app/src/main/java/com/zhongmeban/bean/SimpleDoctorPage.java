package com.zhongmeban.bean;

import com.zhongmeban.bean.postbody.SimpleDoctorPageSourceItems;

import java.util.List;

/**
 * Created by Chengbin He on 2016/8/10.
 */
public class SimpleDoctorPage {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        private int startCount;
        private int totalPage;
        private int pageSize;
        private int endCount;
        private int totalCount;
        private int indexPage;
        private List<SimpleDoctorPageSourceItems> sourceItems;

        public int getStartCount() {
            return startCount;
        }

        public void setStartCount(int startCount) {
            this.startCount = startCount;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getEndCount() {
            return endCount;
        }

        public void setEndCount(int endCount) {
            this.endCount = endCount;
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

        public List<SimpleDoctorPageSourceItems> getSourceItems() {
            return sourceItems;
        }

        public void setSourceItems(List<SimpleDoctorPageSourceItems> sourceItems) {
            this.sourceItems = sourceItems;
        }
    }
}
