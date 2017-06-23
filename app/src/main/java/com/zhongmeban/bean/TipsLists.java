package com.zhongmeban.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:小贴士列表实体
 * user: Chong Chen
 * time：2016/1/22 16:33
 * 邮箱：cchen@ideabinder.com
 */
public class TipsLists extends BaseBean {
    public TipsListData data;

    public TipsListData getData() {
        return data;
    }

    public void setData(TipsListData data) {
        this.data = data;
    }


        public class TipsListData implements Serializable {
        private int startCount;
        private int totalPage;
        private int pageSize;
        private int endCount;
        public ArrayList<TipDataScourceItem> sourceItems;
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

        public List<TipDataScourceItem> getSourceItems() {
            return sourceItems;
        }

//        public void setSourceItems(List<TipDataScourceItem> sourceItems) {
//            this.sourceItems = sourceItems;
//        }

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


    public class TipDataScourceItem implements Serializable {
        private Long createTime;
        private Long updateTime;
        private Long time;
        private int id;
        private int status;
        private boolean isActive;
        private String title;
        private String content;
        private String writer;
        private String urlAddress;
        private boolean isRead;


        public void setCreateTime(Long createTime) {
            this.createTime = createTime;
        }

        public void setUpdateTime(Long updateTime) {
            this.updateTime = updateTime;
        }

        public void setTime(Long time) {
            this.time = time;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public void setActive(boolean active) {
            isActive = active;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setWriter(String writer) {
            this.writer = writer;
        }

        public String getUrlAddress() {
            return urlAddress;
        }

        public void setUrlAddress(String urlAddress) {
            this.urlAddress = urlAddress;
        }

        public void setRead(boolean read) {
            isRead = read;
        }

        public String getWriter() {
            return writer;
        }


        public Long getCreateTime() {
            return createTime;
        }


        public Long getUpdateTime() {
            return updateTime;
        }


        public Long getTime() {
            return time;
        }


        public int getId() {
            return id;
        }


        public int getStatus() {
            return status;
        }


        public boolean isActive() {
            return isActive;
        }


        public String getTitle() {
            return title;
        }


        public String getContent() {
            return content;
        }


        public boolean isRead() {
            return isRead;
        }
    }
}
