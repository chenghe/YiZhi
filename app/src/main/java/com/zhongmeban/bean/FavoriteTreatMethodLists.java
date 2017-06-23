package com.zhongmeban.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:收藏的治疗方法列表
 * user: Chong Chen
 * time：2016/1/16 17:19
 */
public class FavoriteTreatMethodLists extends BaseBean{
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data implements Serializable {
        private List<SourceItems>sourceItems;
        private String startCount;
        private String totalPage;
        private String pageSize;
        private String endCount;
        private String totalCount;
        private String indexPage;

        public List<SourceItems> getSourceItems() {
            return sourceItems;
        }

        public void setSourceItems(List<SourceItems> sourceItems) {
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

        public String getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(String totalCount) {
            this.totalCount = totalCount;
        }

        public String getIndexPage() {
            return indexPage;
        }

        public void setIndexPage(String indexPage) {
            this.indexPage = indexPage;
        }
    }

    public class SourceItems implements Serializable {
        private String name;
        private String id;//治疗方法
        private int categoryId;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }
    }
}
