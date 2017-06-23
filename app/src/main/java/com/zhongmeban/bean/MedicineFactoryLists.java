package com.zhongmeban.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:药厂列表
 * user: Chong Chen
 * time：2016/1/28 14:21
 * 邮箱：cchen@ideabinder.com
 */
public class MedicineFactoryLists extends BaseBean {
    private MedicineFactoryData data;

    public MedicineFactoryData getData() {
        return data;
    }

    public void setData(MedicineFactoryData data) {
        this.data = data;
    }

    public class MedicineFactoryData implements Serializable {
        private int startCount;
        private int totalPage;
        private int endCount;
        private int pageSize;
        private List<MedicineScourceItem> sourceItems;
        private int totalCount;
        private int indexPage;

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

        public List<MedicineScourceItem> getSourceItems() {
            return sourceItems;
        }

        public void setSourceItems(List<MedicineScourceItem> sourceItems) {
            this.sourceItems = sourceItems;
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
    }


}
